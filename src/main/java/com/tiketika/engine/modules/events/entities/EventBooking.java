package com.tiketika.engine.modules.events.entities;

import com.tiketika.engine.common.auditing.BaseAuditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
    @Getter
    @Setter
    @Table(name = "app_event_booking")
    public class EventBooking extends BaseAuditable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String bookingReference;

        private String attendeeName;

        private String attendeeEmail;

        private String attendeePhone;

        private LocalDateTime bookingDate;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "event_id")
        private Event event;
    }

