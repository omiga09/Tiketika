package com.tiketika.Tiketika.common.images.responsibilities;

import com.tiketika.Tiketika.common.auth.enums.EntityType;
import com.tiketika.Tiketika.common.images.entities.Images;
import com.tiketika.Tiketika.common.images.enums.ImageType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Images, Long> {
    List<Images> findByEntityId(Long entityId);
    List<Images> findByEntityType(EntityType entityType);

}
