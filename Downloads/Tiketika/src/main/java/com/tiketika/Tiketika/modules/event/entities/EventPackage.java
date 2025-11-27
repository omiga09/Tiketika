package com.tiketika.Tiketika.modules.event.entities;

import com.tiketika.Tiketika.common.auditing.BaseAuditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(
        name = "app_event_package",
        indexes = {
                @Index(name = "idx_event_package_id", columnList = "id"),
                @Index(name = "idx_event_package_price", columnList = "package_price"),
                @Index(name = "idx_event_package_name", columnList = "package_name")
        }
)
public class EventPackage extends BaseAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String package_price;

    private int packaging_maxCapacity;
    private int package_remained;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean is_package_grove;

    private int package_group_count;

    @Column(columnDefinition = "TEXT")
    private String package_offers;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean is_package_presale;

    private String package_postsale_price;

    private int package_max_comp;
    private int package_remained_comp;

    private Date package_postsale_date;

    private String package_name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;
}
