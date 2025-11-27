package com.tiketika.Tiketika.common.images.entities;

import com.tiketika.Tiketika.common.auditing.BaseAuditable;
import com.tiketika.Tiketika.common.auth.enums.EntityType;
import com.tiketika.Tiketika.common.images.enums.ImageType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
        name = "app_image",
        indexes = {
                @Index(name = "idx_image_id", columnList = "id"),
                @Index(name = "idx_image_entity_type", columnList = "entity_type"),
                @Index(name = "idx_image_entity_id", columnList = "entity_id"),
                @Index(name = "idx_image_type", columnList = "image_type"),
                @Index(name = "idx_image_path", columnList = "image_path")   // NEW INDEX
        }
)
public class Images extends BaseAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Image path is required")
    @Column(nullable = false)
    private String image_path;

    @NotBlank(message = "Image name is required")
    @Column(nullable = false)
    private String image_name;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Entity type is required")
    @Column(nullable = false)
    private EntityType entity_type;

    @NotNull(message = "Sub entity ID is required")
    @Column(nullable = false)
    private Long sub_entity_id;

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Image type is required")
    @Column(nullable = false)
    private ImageType imageType;
}
