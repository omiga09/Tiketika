package com.tiketika.Tiketika.modules.event.entities;

import com.tiketika.Tiketika.common.auditing.BaseAuditable;
import com.tiketika.Tiketika.modules.event.enums.MessageType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
        name = "app_event_message",
        indexes = {
                @Index(name = "idx_event_message_id", columnList = "id"),
                @Index(name = "idx_event_message_type", columnList = "message_type")
        }
)
public class EventMessage extends BaseAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MessageType message_type;

    @Column(columnDefinition = "TEXT")
    private String message_sms_template;

    @Column(columnDefinition = "TEXT")
    private String message_email_template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;
}
