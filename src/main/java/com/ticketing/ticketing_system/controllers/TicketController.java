package com.ticketing.ticketing_system.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.ticketing.ticketing_system.enums.Status;
import com.ticketing.ticketing_system.enums.Priority;
import com.ticketing.ticketing_system.entities.Ticket;
import com.ticketing.ticketing_system.entities.User;
//import com.ticketing.ticketing_system.exceptions.TicketNotFoundException;
import com.ticketing.ticketing_system.repositories.TicketRepository;
import com.ticketing.ticketing_system.repositories.UserRepository;

@RestController
public class TicketController {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/test")
    public String test() {
        return "test done";
    }

    // Get all tickets
    @GetMapping("/tickets")
    public List<Ticket> fetchAllTickets() {
        return ticketRepository.findAll();
    }

    // Get a specific ticket
    @GetMapping("/tickets/{id}")
    public Ticket fetchATicket(@PathVariable("id") int id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found with id: " + id));
    }

    // Get tickets created by a user
    @GetMapping("/{id}/tickets-created")
    public List<Ticket> getTicketsCreatedByUser(@PathVariable("id") int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
        return user.getTicketsCreated();
    }

    // Get tickets assigned to a user
    @GetMapping("/users/{id}/tickets-assigned")
    public List<Ticket> getTicketsAssignedToUser(@PathVariable  int id) {
        User user = userRepository.findById( id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
        return user.getTicketsAssigned();
    }

    // Create a new ticket
    @PostMapping("/tickets")
    @ResponseStatus(HttpStatus.CREATED)
    public Ticket addTicket(@RequestBody Ticket ticket) {
        // Ensure createdBy user exists
        if (ticket.getCreatedBy() != null && ticket.getCreatedBy().getId() != 0) {
            User creator = userRepository.findById(ticket.getCreatedBy().getId())
                    .orElseThrow(() -> new RuntimeException("User not found with id " + ticket.getCreatedBy().getId()));
            ticket.setCreatedBy(creator);
        } else {
            throw new RuntimeException("Ticket must have a valid creator (user_id)");
        }

        // If assignedTo is provided, validate it
        if (ticket.getAssignedTo() != null && ticket.getAssignedTo().getId() != 0) {
            User assignee = userRepository.findById(ticket.getAssignedTo().getId())
                    .orElseThrow(() -> new RuntimeException("Assigned user not found with id " + ticket.getAssignedTo().getId()));
            ticket.setAssignedTo(assignee);
        }

        return ticketRepository.save(ticket);
    }

    // // Update ticket
    // @PutMapping("/tickets/{id}")
    // public Ticket updateTicket(@PathVariable("id") int id, @RequestBody Ticket ticketDetails) {
    //     return ticketRepository.findById(id).map(ticket -> {
    //         ticket.setTitle(ticketDetails.getTitle());
    //         ticket.setStatus(ticketDetails.getStatus());
    //         ticket.setDescription(ticketDetails.getDescription());
    //         ticket.setPriority(ticketDetails.getPriority());
    //         ticket.setCategory(ticketDetails.getCategory());

    //         // Handle assignment if provided
    //         if (ticketDetails.getAssignedTo() != null && ticketDetails.getAssignedTo().getId() != null) {
    //             User assignee = userRepository.findById(ticketDetails.getAssignedTo().getId())
    //                     .orElseThrow(() -> new RuntimeException("Assigned user not found with id " + ticketDetails.getAssignedTo().getId()));
    //             ticket.setAssignedTo(assignee);
    //         }

    //         return ticketRepository.save(ticket);
    //     }).orElseThrow(() -> new TicketNotFoundException("Ticket not found with id " + id));
    // }

    // Assign a ticket to a user (agent)
    @PutMapping("/tickets/{id}/assign/{userId}")
    public Ticket assignTicket(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id " + id));
        User assignee = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));
        ticket.setAssignedTo(assignee);
        return ticketRepository.save(ticket);
    }

    // Delete ticket
    @DeleteMapping("/tickets/{id}")
    public String deleteTicket(@PathVariable("id") int id) {
        ticketRepository.deleteById(id);
        return "Ticket deleted successfully";
    }

    // Get all tickets by a particular status
    @GetMapping("/tickets/status/{status}")
    public List<Ticket> getTicketsbyStatus(@PathVariable Status status) {
        return ticketRepository.findByStatus(status);
    }

    // Sort all tickets by status enum order
    @GetMapping("/tickets/sorted/status")
    public List<Ticket> getAllTicketsSortedByStatus() {
        return ticketRepository.findAll(org.springframework.data.domain.Sort.by("status"));
    }

    // Get tickets according to status in latest to oldest order
    @GetMapping("/tickets/status/{status}/sortedByDate")
    public List<Ticket> getTicketsByStatusSortedByDate(@PathVariable Status status) {
        return ticketRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    // Get tickets by a priority and sort by latest date
    @GetMapping("/tickets/priority/{priority}/sortedByDate")
    public List<Ticket> getTicketsByPrioritySortedByDate(@PathVariable Priority priority) {
        return ticketRepository.findByPriorityOrderByCreatedAtDesc(priority);
    }
}
