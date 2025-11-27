package com.tiketika.engine.common.images.responsibilities;

import com.tiketika.engine.common.auth.enums.EntityType;
import com.tiketika.engine.common.images.entities.Images;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Images, Long> {
    List<Images> findByEntityId(Long entityId);
    List<Images> findByEntityType(EntityType entityType);

}
