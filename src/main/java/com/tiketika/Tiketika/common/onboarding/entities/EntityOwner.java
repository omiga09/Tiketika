package com.tiketika.Tiketika.common.onboarding.entities;

import com.tiketika.Tiketika.common.auditing.BaseAuditable;
import com.tiketika.Tiketika.common.auth.entities.UserEntity;
import com.tiketika.Tiketika.common.auth.enums.EntityType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "app_entity_owner")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntityOwner extends BaseAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Owner type is required")
    private EntityType entityType;


    @OneToOne(mappedBy = "entityOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    private KYCDetail kycDetail;


    @OneToMany(mappedBy = "entity_owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserEntity> userEntities;

}
