package com.tiketika.engine.modules.events.repositories;

import com.tiketika.engine.modules.events.entities.EventTransactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventTransactionRepository extends JpaRepository<EventTransactions, Long> {
    List<EventTransactions> findByEventId(Long eventId);
    List<EventTransactions> findByBookingId(Long bookingId);

}