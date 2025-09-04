package com.ticketing.ticketing_system.repositories;

import com.ticketing.ticketing_system.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ticketing.ticketing_system.enums.Status;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    //find all tickets by status
    List<Ticket> findByStatus(Status status);
    
    //find tickets by status and order them
    List<Ticket> findByStatusOrderByCreatedAtDesc(Status status);

    
}
