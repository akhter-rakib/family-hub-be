package com.familyhub.report.service;

import com.familyhub.bill.repository.BillRepository;
import com.familyhub.family.repository.FamilyMemberRepository;
import com.familyhub.family.service.FamilyService;
import com.familyhub.inventory.repository.InventoryItemRepository;
import com.familyhub.notification.repository.NotificationRepository;
import com.familyhub.common.security.SecurityUtils;
import com.familyhub.purchase.repository.PurchaseRepository;
import com.familyhub.report.dto.*;
import com.familyhub.shopping.entity.ShoppingStatus;
import com.familyhub.shopping.repository.ShoppingRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final PurchaseRepository purchaseRepo;
    private final BillRepository billRepo;
    private final FamilyMemberRepository memberRepo;
    private final ShoppingRequestRepository shoppingRepo;
    private final InventoryItemRepository inventoryRepo;
    private final NotificationRepository notificationRepo;
    private final FamilyService familyService;

    public MonthlyReportDto getMonthlyReport(UUID familyId, int year, int month) {
        familyService.validateMembership(familyId);

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.plusMonths(1).minusDays(1);

        BigDecimal totalPurchases = purchaseRepo.sumCostByFamilyIdAndDateRange(familyId, start, end);
        BigDecimal totalBills = billRepo.sumPaidBillsByDateRange(familyId, start, end);

        List<MonthlyReportDto.CategoryBreakdown> categoryBreakdown =
                purchaseRepo.sumCostByCategoryAndDateRange(familyId, start, end).stream()
                        .map(row -> MonthlyReportDto.CategoryBreakdown.builder()
                                .category(row[0] != null ? (String) row[0] : "Uncategorized")
                                .amount((BigDecimal) row[1])
                                .build())
                        .toList();

        List<MonthlyReportDto.ItemBreakdown> itemBreakdown =
                purchaseRepo.sumCostByItemAndDateRange(familyId, start, end).stream()
                        .map(row -> MonthlyReportDto.ItemBreakdown.builder()
                                .itemName((String) row[0])
                                .amount((BigDecimal) row[1])
                                .totalQuantity((BigDecimal) row[2])
                                .build())
                        .toList();

        List<MonthlyReportDto.MemberBreakdown> memberBreakdown =
                purchaseRepo.sumCostByMemberAndDateRange(familyId, start, end).stream()
                        .map(row -> MonthlyReportDto.MemberBreakdown.builder()
                                .memberName((String) row[0])
                                .amount((BigDecimal) row[1])
                                .build())
                        .toList();

        return MonthlyReportDto.builder()
                .year(year)
                .month(month)
                .totalPurchaseCost(totalPurchases)
                .totalBillsPaid(totalBills)
                .grandTotal(totalPurchases.add(totalBills))
                .categoryBreakdown(categoryBreakdown)
                .itemBreakdown(itemBreakdown)
                .memberBreakdown(memberBreakdown)
                .build();
    }

    @Cacheable(value = "dashboard", key = "#familyId")
    public DashboardDto getDashboard(UUID familyId) {
        familyService.validateMembership(familyId);
        UUID userId = SecurityUtils.getCurrentUserId();

        LocalDate now = LocalDate.now();
        LocalDate monthStart = now.withDayOfMonth(1);

        int totalMembers = memberRepo.findActiveMembersByFamilyId(familyId).size();
        int pendingRequests = shoppingRepo.findByFamilyIdAndStatus(familyId, ShoppingStatus.PENDING).size();
        int overdueBills = billRepo.findByFamilyIdAndPaidFalseAndDueDateBeforeOrderByDueDateAsc(familyId, now).size();
        BigDecimal monthlyPurchases = purchaseRepo.sumCostByFamilyIdAndDateRange(familyId, monthStart, now);
        BigDecimal monthlyBills = billRepo.sumPaidBillsByDateRange(familyId, monthStart, now);
        BigDecimal monthlySpending = monthlyPurchases.add(monthlyBills);
        int lowStockItems = inventoryRepo.findLowStock(familyId).size();
        int unreadNotifications = (int) notificationRepo.countUnreadByUserId(userId);

        return DashboardDto.builder()
                .totalMembers(totalMembers)
                .pendingShoppingRequests(pendingRequests)
                .overdueBills(overdueBills)
                .monthlySpending(monthlySpending)
                .lowStockItems(lowStockItems)
                .unreadNotifications(unreadNotifications)
                .build();
    }
}
