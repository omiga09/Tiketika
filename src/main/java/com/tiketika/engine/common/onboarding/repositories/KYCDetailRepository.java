package com.tiketika.engine.common.onboarding.repositories;

import com.tiketika.engine.common.onboarding.entities.KYCDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KYCDetailRepository extends JpaRepository<KYCDetail, Long> {
    Optional<KYCDetail> findByEntityOwnerId(Long entityOwnerId);

}
