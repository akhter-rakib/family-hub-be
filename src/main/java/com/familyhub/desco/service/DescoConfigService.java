package com.familyhub.desco.service;

import com.familyhub.desco.dto.DescoConfigDto;
import com.familyhub.desco.entity.DescoConfig;
import com.familyhub.desco.repository.DescoConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DescoConfigService {
    private final DescoConfigRepository descoConfigRepository;

    public Optional<DescoConfigDto> getConfig(UUID familyId) {
        return descoConfigRepository.findByFamilyId(familyId).map(this::toDto);
    }

    @Transactional
    public DescoConfigDto saveOrUpdate(UUID familyId, String accountNo, String meterNo, boolean enabled) {
        DescoConfig config = descoConfigRepository.findByFamilyId(familyId)
                .orElse(DescoConfig.builder().familyId(familyId).build());
        config.setAccountNo(accountNo);
        config.setMeterNo(meterNo);
        config.setEnabled(enabled);
        return toDto(descoConfigRepository.save(config));
    }

    @Transactional
    public void delete(UUID familyId) {
        descoConfigRepository.findByFamilyId(familyId).ifPresent(descoConfigRepository::delete);
    }

    private DescoConfigDto toDto(DescoConfig config) {
        return DescoConfigDto.builder()
                .id(config.getId())
                .familyId(config.getFamilyId())
                .accountNo(config.getAccountNo())
                .meterNo(config.getMeterNo())
                .enabled(config.isEnabled())
                .build();
    }
}
