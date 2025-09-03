package com.ticketing.ticketing_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketing.ticketing_system.entities.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
