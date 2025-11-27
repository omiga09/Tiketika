package com.tiketika.Tiketika.modules.event.repositories;

import com.tiketika.Tiketika.modules.event.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

}