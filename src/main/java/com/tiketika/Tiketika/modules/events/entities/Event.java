package com.tiketika.Tiketika.modules.events.entities;

import com.tiketika.Tiketika.common.auditing.BaseAuditable;
import com.tiketika.Tiketika.modules.events.enums.Event_payment_terms;
import com.tiketika.Tiketika.modules.events.enums.Event_recurring_by;
import com.tiketika.Tiketika.modules.events.enums.Event_recurring_for;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@Table(
        name = "app_event",
        indexes = {
                @Index(name = "idx_event_id", columnList = "id"),
                @Index(name = "idx_event_name", columnList = "event_name"),
                @Index(name = "idx_event_subdomain", columnList = "event_subdomain"),
                @Index(name = "idx_event_description", columnList = "event_description")
        }
)
public class Event extends BaseAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String event_name;

    private String event_venue;
    private String event_country;

    @Column(nullable = false, unique = true)
    private String event_subdomain;

    private String event_start_time;
    private String event_end_time;
    private String event_start_date;
    private String event_end_date;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean is_event_cashless;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean is_event_active;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean is_event_recurring;

    @Enumerated(EnumType.STRING)
    private Event_recurring_by event_recurring_by;

    @Enumerated(EnumType.STRING)
    private Event_recurring_for event_recurring_for;

    @Enumerated(EnumType.STRING)
    private Event_payment_terms event_payment_terms;

    private BigDecimal event_payment_percent;
    private BigDecimal event_payment_flat;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean event_has_games;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean event_has_purchases;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean is_game_active;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean is_purchase_active;

    @Column(columnDefinition = "TEXT")
    private String event_description;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EventPackage> eventPackages;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EventMessage> eventMessages;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EventTransactions> eventTransactions;
}
