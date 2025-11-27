package com.tiketika.engine.common.onboarding.controller;


import com.tiketika.engine.common.auth.enums.EntityType;
import com.tiketika.engine.common.auth.repositories.EntityOwnerRepository;
import com.tiketika.engine.common.helper.KYCValidator;
import com.tiketika.engine.common.images.entities.Images;
import com.tiketika.engine.common.images.enums.ImageType;
import com.tiketika.engine.common.onboarding.entities.EntityOwner;
import com.tiketika.engine.common.onboarding.entities.KYCDetail;
import com.tiketika.engine.common.onboarding.services.KYCUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/onboarding/kyc")
@RequiredArgsConstructor
public class KYCController {

    private final EntityOwnerRepository entityOwnerRepository;
    private final KYCUploadService kycUploadService;

    @PostMapping("/submit/{ownerId}")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('EVENT_OWNER') or hasRole('BUS_OWNER') or hasRole('TOUR_OWNER') or hasRole('HOTSPOT_OWNER')")
    public ResponseEntity<String> submitKyc(@PathVariable Long ownerId, @RequestBody KYCDetail kyc) {
        EntityOwner owner = entityOwnerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("EntityOwner not found"));

        // Validate kyc by group type
        KYCValidator.validate(kyc);

        // Link and save
        kyc.setEntityOwner(owner);
        owner.setKycDetail(kyc);
        entityOwnerRepository.save(owner);

        return ResponseEntity.ok("KYC submitted");
    }

    @PostMapping("/{ownerId}/upload")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('EVENT_OWNER') or hasRole('BUS_OWNER') or hasRole('TOUR_OWNER') or hasRole('HOTSPOT_OWNER')")
    public ResponseEntity<List<Images>> uploadKycImages(
            @PathVariable Long ownerId,
            @RequestParam EntityType entityType,
            @RequestParam(required = false) Long subEntityId,
            @RequestParam(required = false) MultipartFile profile,
            @RequestParam(required = false) MultipartFile nidaFront,
            @RequestParam(required = false) MultipartFile nidaBack,
            @RequestParam(required = false) MultipartFile license,
            @RequestParam(required = false) MultipartFile tinCert,
            @RequestParam(required = false) MultipartFile vrnDoc,
            @RequestParam(required = false) MultipartFile logo
    ) {
        Map<ImageType, MultipartFile> files = new LinkedHashMap<>();
        if (profile != null) files.put(ImageType.PROFILE, profile);
        if (nidaFront != null) files.put(ImageType.NIDA_FRONT, nidaFront);
        if (nidaBack != null) files.put(ImageType.NIDA_BACK, nidaBack);
        if (license != null) files.put(ImageType.LICENSE, license);
        if (tinCert != null) files.put(ImageType.TIN_CERT, tinCert);
        if (vrnDoc != null) files.put(ImageType.VRN_DOC, vrnDoc);
        if (logo != null) files.put(ImageType.LOGO, logo);

        List<Images> saved = kycUploadService.uploadKycFiles(ownerId, entityType, subEntityId, files);
        return ResponseEntity.ok(saved);
    }
}
