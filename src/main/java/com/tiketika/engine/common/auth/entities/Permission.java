package com.tiketika.engine.common.auth.entities;

import com.tiketika.engine.common.auditing.BaseAuditable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "app_permission",
        uniqueConstraints = @UniqueConstraint(columnNames = "name")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Permission extends BaseAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String endpoint;

    @Column(nullable = false)
    private String method;

    private String description;
}
