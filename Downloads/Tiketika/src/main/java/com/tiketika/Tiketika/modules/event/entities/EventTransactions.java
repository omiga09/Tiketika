package com.tiketika.Tiketika.modules.event.entities;

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
        name="app_event_transaction",
        indexes = {
                @Index(name = "idx_event_tx_id", columnList = "id"),
                @Index(name = "idx_event_tx_buyer_email", columnList = "buyer_email"),
                @Index(name = "idx_event_tx_vendor_name", columnList = "vendor_name"),
                @Index(name = "idx_event_tx_event_id", columnList = "event_id"),
                @Index(name = "idx_event_tx_vendor_id", columnList = "vendor_id")
        }
)
public class EventTransactions extends BaseAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tx_item_name;
    private BigDecimal tx_item_amount;
    private int tx_item_quantity;

    private String buyer_name;

    @Column(length = 255)
    private String buyer_email;

    private String buyer_phone;

    private String vendor_name;
    private String vendor_email;
    private String vendor_phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private EventBooking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="vendor_id")
    private UserEntity vendor;
}
