package com.tiketika.Tiketika.common.onboarding.services;

import com.tiketika.Tiketika.common.auth.enums.EntityType;
import com.tiketika.Tiketika.common.auth.repositories.EntityOwnerRepository;
import com.tiketika.Tiketika.common.helper.ImageCategoryMapper;
import com.tiketika.Tiketika.common.images.entities.Images;
import com.tiketika.Tiketika.common.images.enums.ImageType;

import com.tiketika.Tiketika.common.images.responsibilities.ImageRepository;
import com.tiketika.Tiketika.common.onboarding.entities.EntityOwner;
import com.tiketika.Tiketika.common.services.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KYCUploadService {

    private final FileStorageService fileStorageService;
    private final ImageRepository imagesRepository;
    private final EntityOwnerRepository entityOwnerRepository;

    public Images uploadKycFile(Long entityOwnerId,
                                EntityType entityType,
                                Long subEntityId,
                                MultipartFile file,
                                ImageType imageType) {

        EntityOwner owner = entityOwnerRepository.findById(entityOwnerId)
                .orElseThrow(() -> new RuntimeException("EntityOwner not found"));

        String category = ImageCategoryMapper.categoryFor(imageType);
        String savedPath = fileStorageService.saveFile(file, category);

        Images img = new Images();
        img.setImage_name(file.getOriginalFilename() != null ? file.getOriginalFilename() : imageType.name());
        img.setImage_path(savedPath);
        img.setEntity_type(entityType);
        img.setEntityId(owner.getId());
        img.setSub_entity_id(subEntityId != null ? subEntityId : owner.getId());
        img.setImageType(imageType);

        return imagesRepository.save(img);
    }

    public List<Images> uploadKycFiles(Long entityOwnerId,
                                       EntityType entityType,
                                       Long subEntityId,
                                       Map<ImageType, MultipartFile> files) {

        List<Images> results = new ArrayList<>();
        for (Map.Entry<ImageType, MultipartFile> entry : files.entrySet()) {
            results.add(uploadKycFile(entityOwnerId, entityType, subEntityId, entry.getValue(), entry.getKey()));
        }
        return results;
    }
}
