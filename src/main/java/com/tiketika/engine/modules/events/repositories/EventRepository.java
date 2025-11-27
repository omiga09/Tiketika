package com.tiketika.engine.modules.events.repositories;

import com.tiketika.engine.modules.events.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

}