package com.tiketika.Tiketika.modules.events.entities;

import com.tiketika.Tiketika.common.auditing.BaseAuditable;
import com.tiketika.Tiketika.common.auth.entities.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(
        name="app_event_product",
        indexes = {
                @Index(name = "idx_product_name", columnList = "product_name"),
                @Index(name = "idx_product_price", columnList = "product_price"),
                @Index(name = "idx_product_vendor_id", columnList = "vendor_id")
        }
)
public class Product extends BaseAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String product_name;

    private BigDecimal product_price;

    @Column(columnDefinition = "TEXT")
    private String product_description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private UserEntity vendor;
}
