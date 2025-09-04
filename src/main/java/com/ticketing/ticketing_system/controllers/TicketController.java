package com.ticketing.ticketing_system.controllers;

import java.util.List;
//import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.ticketing.ticketing_system.enums.Status;
import com.ticketing.ticketing_system.enums.Priority;

import com.ticketing.ticketing_system.entities.Ticket;
import com.ticketing.ticketing_system.entities.User;
import com.ticketing.ticketing_system.repositories.TicketRepository;
import com.ticketing.ticketing_system.repositories.UserRepository;

@RestController
public class TicketController {
    
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/test")
    public String test(){
        return "test done";
    }
    
    // Get all tickets
    @GetMapping("/tickets")
    public List<Ticket> fetchAllTickets(){
        return ticketRepository.findAll();
    }
    
     @GetMapping("/tickets/{id}")
    public Ticket fetchATicket(@PathVariable("id") int id) {
        return ticketRepository.findById(id)
            .orElseThrow(() -> new TicketNotFoundException("Ticket not found with id: " + id));
    }

     @PostMapping("/tickets")
    // Get a ticket by ID
    @GetMapping("/tickets/{id}")
    public Ticket fetchATicket(@PathVariable("id") Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id " + id));
    }

    // Get tickets created by a user
    @GetMapping("/{id}/tickets-created")
    public List<Ticket> getTicketsCreatedByUser(@PathVariable Long id) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    return user.getTicketsCreated();
    }

   // Get tickets assigned to a user
   @GetMapping("/{id}/tickets-assigned")
    public List<Ticket> getTicketsAssignedToUser(@PathVariable Long id) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    return user.getTicketsAssigned();
     }


    // Create a new ticket
    @PostMapping("/tickets")
    @ResponseStatus(HttpStatus.CREATED)
    public Ticket addTicket(@RequestBody Ticket ticket) {
        // Ensure createdBy user exists
        if (ticket.getCreatedBy() != null && ticket.getCreatedBy().getId() != null) {
            User creator = userRepository.findById(ticket.getCreatedBy().getId())
                    .orElseThrow(() -> new RuntimeException("User not found with id " + ticket.getCreatedBy().getId()));
            ticket.setCreatedBy(creator);
        } else {
            throw new RuntimeException("Ticket must have a valid creator (user_id)");
        }

        // If assignedTo is provided, validate it
        if (ticket.getAssignedTo() != null && ticket.getAssignedTo().getId() != null) {
            User assignee = userRepository.findById(ticket.getAssignedTo().getId())
                    .orElseThrow(() -> new RuntimeException("Assigned user not found with id " + ticket.getAssignedTo().getId()));
            ticket.setAssignedTo(assignee);
        }

        return ticketRepository.save(ticket);
    }

    // Update ticket
    @PutMapping("/tickets/{id}")
    public Ticket updateTicket(@PathVariable("id") Long id, @RequestBody Ticket ticketDetails) {
        return ticketRepository.findById(id).map(ticket -> {
            ticket.setTitle(ticketDetails.getTitle());
            ticket.setStatus(ticketDetails.getStatus());
            ticket.setDescription(ticketDetails.getDescription());
            ticket.setPriority(ticketDetails.getPriority());
            ticket.setCategory(ticketDetails.getCategory());

            // Handle assignment if provided
            if (ticketDetails.getAssignedTo() != null && ticketDetails.getAssignedTo().getId() != null) {
                User assignee = userRepository.findById(ticketDetails.getAssignedTo().getId())
                        .orElseThrow(() -> new RuntimeException("Assigned user not found with id " + ticketDetails.getAssignedTo().getId()));
                ticket.setAssignedTo(assignee);
            }

            return ticketRepository.save(ticket);
        }).orElseThrow(() -> new TicketNotFoundException("Ticket not found with id " + id));
    }

    // Assign a ticket to a user (agent)
    @PutMapping("/tickets/{id}/assign/{userId}")
    public Ticket assignTicket(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id " + id));
        User assignee = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));
        ticket.setAssignedTo(assignee);
        return ticketRepository.save(ticket);
    }

    // Delete ticket
    @DeleteMapping("/tickets/{id}")
    public String deleteTicket(@PathVariable("id") Long id) {
        ticketRepository.deleteById(id);
        return "Ticket deleted successfully";
    }

    //get all tickets by a particular status
    @GetMapping("/tickets/status/{status}")
    public List<Ticket> getTicketsbyStatus(@PathVariable Status status){
       return ticketRepository.findByStatus(status);
    }


    //sort all tickets by status enum order
    @GetMapping("/tickets/sorted/status")
    public List<Ticket> getAllTicketsSortedByStatus(){
        return ticketRepository.findAll(org.springframework.data.domain.Sort.by("status"));
    }
 
    //get tickets according to status in latest to oldest order
    @GetMapping("/tickets/status/{status}/sortedByDate")
    public List<Ticket> getTicketsByStatusSortedByDate(@PathVariable Status status){
        return ticketRepository.findByStatusOrderByCreatedAtDesc(status);
    }
    
    //get tickets by a priority ans sort by latest date
    @GetMapping("/tickets/priority/{priority}/sortedByDate")
    public List<Ticket> getTicketsByPrioritySortedByDate(@PathVariable Priority priority){
        return ticketRepository.findByPriorityOrderByCreatedAtDesc(priority);
    }
}
