package com.tiketika.Tiketika.modules.event.repositories;

import com.tiketika.Tiketika.modules.event.entities.EventMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventMessageRepository extends JpaRepository<EventMessage, Long> {
    List<EventMessage> findByEventId(Long eventId);
}