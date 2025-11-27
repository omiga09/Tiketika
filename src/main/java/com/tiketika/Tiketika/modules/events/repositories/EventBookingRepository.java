package com.tiketika.Tiketika.modules.events.repositories;


import com.tiketika.Tiketika.modules.events.entities.EventBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventBookingRepository extends JpaRepository<EventBooking, Long> {
    List<EventBooking> findByEventId(Long eventId);
    EventBooking findByBookingReference(String bookingReference);
}
