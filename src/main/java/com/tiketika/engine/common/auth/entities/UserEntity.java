package com.tiketika.engine.common.auth.entities;

import com.tiketika.engine.common.auditing.BaseAuditable;
import com.tiketika.engine.common.auth.enums.EntityType;
import com.tiketika.engine.common.onboarding.entities.EntityOwner;
import com.tiketika.engine.modules.events.entities.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter
@Table(
        name = "app_user_entity",
        indexes = {
                @Index(name = "idx_user_entity_id", columnList = "id"),
                @Index(name = "idx_user_entity_type", columnList = "entity_type"),
                @Index(name = "idx_user_entity_owner", columnList = "owner_id")
        }
)
public class UserEntity extends BaseAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false)
    private EntityType entityType;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_entity_roles",
            joinColumns = @JoinColumn(name = "user_entity_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private EntityOwner entity_owner;

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();
}
