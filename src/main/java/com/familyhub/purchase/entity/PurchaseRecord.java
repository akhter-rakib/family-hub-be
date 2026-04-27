package com.familyhub.purchase.entity;

import com.familyhub.auth.entity.User;
import com.familyhub.catalog.entity.Item;
import com.familyhub.catalog.entity.Unit;
import com.familyhub.common.entity.BaseEntity;
import com.familyhub.shopping.entity.ShoppingRequest;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "purchase_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseRecord extends BaseEntity {

    @Column(name = "family_id", nullable = false)
    private UUID familyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "quantity", nullable = false, precision = 10, scale = 3)
    private BigDecimal quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;

    @Column(name = "normalized_quantity", precision = 15, scale = 6)
    private BigDecimal normalizedQuantity;

    @Column(name = "cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal cost;

    @Column(name = "shop_name")
    private String shopName;

    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @Column(name = "receipt_url")
    private String receiptUrl;

    @Column(name = "note")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchased_by", nullable = false)
    private User purchasedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_request_id")
    private ShoppingRequest shoppingRequest;
}
