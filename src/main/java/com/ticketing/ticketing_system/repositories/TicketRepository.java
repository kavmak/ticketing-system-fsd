package com.ticketing.ticketing_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD

import com.ticketing.ticketing_system.entities.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
=======
import com.ticketing.ticketing_system.enums.Status;
import java.util.List;
import com.ticketing.ticketing_system.enums.Priority;
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    //find all tickets by status
    List<Ticket> findByStatus(Status status);
    
    //find tickets by status and order them
    List<Ticket> findByStatusOrderByCreatedAtDesc(Status status);

    //find tickets by priority sorted from latest date
    List<Ticket> findByPriorityOrderByCreatedAtDesc(Priority priority);
>>>>>>> 909dd99183be06e892b0bc64b06a42957ac4f0cc
}
