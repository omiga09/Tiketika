package com.tiketika.engine.modules.events.repositories;

import com.tiketika.engine.modules.events.entities.EventMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventMessageRepository extends JpaRepository<EventMessage, Long> {
    List<EventMessage> findByEventId(Long eventId);
}