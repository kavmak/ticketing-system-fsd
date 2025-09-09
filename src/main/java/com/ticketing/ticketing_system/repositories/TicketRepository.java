package com.ticketing.ticketing_system.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketing.ticketing_system.entities.Ticket;
import com.ticketing.ticketing_system.enums.Priority;
import com.ticketing.ticketing_system.enums.Status;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    // Find all tickets by status
    List<Ticket> findByStatus(Status status);

    // Find tickets by status and order by latest
    List<Ticket> findByStatusOrderByCreatedAtDesc(Status status);

    // Find tickets by priority and order by latest
    List<Ticket> findByPriorityOrderByCreatedAtDesc(Priority priority);
}
