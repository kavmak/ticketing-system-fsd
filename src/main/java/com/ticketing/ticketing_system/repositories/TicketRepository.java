package com.ticketing.ticketing_system.repositories;

import com.ticketing.ticketing_system.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
