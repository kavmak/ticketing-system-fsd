package com.ticketing.ticketing_system.repositories;

import com.ticketing.ticketing_system.entities.Ticket;
import com.ticketing.ticketing_system.enums.Status;
import com.ticketing.ticketing_system.enums.Priority;
import com.ticketing.ticketing_system.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByStatus(Status status);

    Page<Ticket> findByStatus(Status status, Pageable pageable);

    List<Ticket> findByPriority(Priority priority);

    Page<Ticket> findByPriority(Priority priority, Pageable pageable);

    List<Ticket> findByCategory(Category category);

    Page<Ticket> findByCategory(Category category, Pageable pageable);

    List<Ticket> findByStatusOrderByCreatedAtDesc(Status status);

    List<Ticket> findByPriorityOrderByCreatedAtDesc(Priority priority);

    // ... other methods ...
}