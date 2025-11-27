package com.tiketika.Tiketika.modules.event.repositories;


import com.tiketika.Tiketika.modules.event.entities.EventPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventPackageRepository extends JpaRepository<EventPackage, Long> {
    List<EventPackage> findByEventId(Long eventId);
}