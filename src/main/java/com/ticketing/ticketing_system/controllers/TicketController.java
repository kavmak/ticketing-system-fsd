package com.ticketing.ticketing_system.controllers;

import com.ticketing.ticketing_system.entities.Ticket;
import com.ticketing.ticketing_system.repositories.TicketRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketRepository ticketRepo;

    public TicketController(TicketRepository ticketRepo) {
        this.ticketRepo = ticketRepo;
    }

    @PostMapping
    public Ticket create(@RequestBody Ticket ticket) {
        return ticketRepo.save(ticket);
    }

    @GetMapping
    public List<Ticket> all() {
        return ticketRepo.findAll();
    }
}
