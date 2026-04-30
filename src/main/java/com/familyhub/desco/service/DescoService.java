package com.familyhub.desco.service;

import com.familyhub.desco.dto.DescoApiResponse;
import com.familyhub.desco.dto.DescoBalanceDto;
import com.familyhub.desco.entity.DescoBalance;
import com.familyhub.desco.repository.DescoBalanceRepository;
import com.familyhub.desco.repository.DescoConfigRepository;
import com.familyhub.desco.entity.DescoConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class DescoService {

    private static final BigDecimal LOW_BALANCE_THRESHOLD = new BigDecimal("200");
    private static final int MAX_RETRIES = 5;
    private static final DateTimeFormatter READING_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final DescoBalanceRepository descoBalanceRepository;
    private final DescoConfigRepository descoConfigRepository;

    /**
     * Scheduled to run daily at 9 PM (Bangladesh time, UTC+6).
     * Retries up to 5 times on failure before giving up for the day.
     */

    @Scheduled(cron = "0 0 21 * * *", zone = "Asia/Dhaka")
    public void fetchDescoBalanceScheduled() {
        log.info("Starting scheduled DESCO balance fetch for all enabled configs");
        for (DescoConfig config : descoConfigRepository.findAllByEnabledTrue()) {
            for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
                try {
                    fetchAndSaveBalance(config);
                    log.info("DESCO balance fetched for family {} on attempt {}", config.getFamilyId(), attempt);
                    break;
                } catch (Exception e) {
                    log.warn("DESCO API call failed for family {} (attempt {}/{}): {}", config.getFamilyId(), attempt, MAX_RETRIES, e.getMessage());
                    if (attempt < MAX_RETRIES) {
                        try {
                            Thread.sleep(30_000L);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            log.error("Retry sleep interrupted, stopping retries for family {}");
                            break;
                        }
                    }
                }
            }
        }
    }

    public void fetchAndSaveBalance(DescoConfig config) {
        RestClient restClient = RestClient.builder()
                .baseUrl("https://prepaid.desco.org.bd")
                .requestFactory(new JdkClientHttpRequestFactory(
                        HttpClient.newBuilder().sslContext(createTrustAllSslContext()).build()))
                .build();

        DescoApiResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/tkdes/customer/getBalance")
                        .queryParam("accountNo", config.getAccountNo())
                        .queryParam("meterNo", config.getMeterNo())
                        .build())
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.REFERER, "https://prepaid.desco.org.bd/customer/")
                .retrieve()
                .body(DescoApiResponse.class);

        if (response == null || response.getCode() != 200 || response.getData() == null) {
            throw new RuntimeException("DESCO API returned invalid response");
        }

        DescoApiResponse.DescoData data = response.getData();

        DescoBalance entity = DescoBalance.builder()
                .familyId(config.getFamilyId())
                .accountNo(data.getAccountNo())
                .meterNo(data.getMeterNo())
                .balance(data.getBalance())
                .currentMonthConsumption(data.getCurrentMonthConsumption())
                .readingTime(LocalDateTime.parse(data.getReadingTime(), READING_TIME_FORMAT))
                .fetchedAt(LocalDateTime.now())
                .build();

        descoBalanceRepository.save(entity);
        log.info("DESCO balance saved for family {}: {} BDT, consumption: {} kWh", config.getFamilyId(), data.getBalance(), data.getCurrentMonthConsumption());
    }

    public void fetchAndSaveBalanceForFamily(java.util.UUID familyId) {
        descoConfigRepository.findByFamilyId(familyId).filter(DescoConfig::isEnabled).ifPresent(this::fetchAndSaveBalance);
    }

    public DescoBalanceDto getLatestBalance(java.util.UUID familyId) {
        return descoBalanceRepository.findTopByFamilyIdOrderByFetchedAtDesc(familyId)
                .map(this::toDto)
                .orElse(null);
    }

    private DescoBalanceDto toDto(DescoBalance entity) {
        return DescoBalanceDto.builder()
                .id(entity.getId())
                .accountNo(entity.getAccountNo())
                .meterNo(entity.getMeterNo())
                .balance(entity.getBalance())
                .currentMonthConsumption(entity.getCurrentMonthConsumption())
                .readingTime(entity.getReadingTime())
                .fetchedAt(entity.getFetchedAt())
                .lowBalance(entity.getBalance().compareTo(LOW_BALANCE_THRESHOLD) < 0)
                .build();
    }

    private SSLContext createTrustAllSslContext() {
        try {
            TrustManager[] trustAll = { new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                public void checkServerTrusted(X509Certificate[] certs, String authType) {}
            }};
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAll, new SecureRandom());
            return sslContext;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create SSL context", e);
        }
    }
}
