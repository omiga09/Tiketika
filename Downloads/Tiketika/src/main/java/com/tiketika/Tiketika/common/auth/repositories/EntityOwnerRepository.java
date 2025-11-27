package com.tiketika.Tiketika.common.auth.repositories;

import com.tiketika.Tiketika.common.onboarding.entities.EntityOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntityOwnerRepository extends JpaRepository<EntityOwner, Long> {
    Optional<EntityOwner> findById(Long id);

}
