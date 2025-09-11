package com.ticketing.ticketing_system.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.ticketing.ticketing_system.enums.Status;
import com.ticketing.ticketing_system.enums.Priority;
import com.ticketing.ticketing_system.enums.Role;
import com.ticketing.ticketing_system.entities.Ticket;
import com.ticketing.ticketing_system.entities.User;
import com.ticketing.ticketing_system.repositories.TicketRepository;
import com.ticketing.ticketing_system.repositories.UserRepository;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
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
    @Cacheable("tickets")
    @GetMapping("/tickets")
    public Page<Ticket> fetchAllTickets(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        PageRequest pageable = PageRequest.of(page,size);
        return ticketRepository.findAll(pageable);
    }

    // Get a specific ticket
    @GetMapping("/tickets/{id}")
    public Ticket fetchATicket(@PathVariable("id") int id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));
    }

    // Get tickets created by a user
    @GetMapping("/users/{id}/tickets-created")
    public List<Ticket> getTicketsCreatedByUser(@PathVariable("id") int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
        return user.getTicketsCreated();
    }

    // Get tickets assigned to a user
    @GetMapping("/users/{id}/tickets-assigned")
    public List<Ticket> getTicketsAssignedToUser(@PathVariable int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
        return user.getTicketsAssigned();
    }

    // Create a new ticket (only USER role allowed)
    @PostMapping("/tickets")
    @ResponseStatus(HttpStatus.CREATED)
    public Ticket addTicket(@RequestBody Ticket ticket) {
        if (ticket.getCreatedBy() == null || ticket.getCreatedBy().getId() == 0) {
            throw new RuntimeException("Ticket must have a valid creator (user_id)");
        }

        User creator = userRepository.findById(ticket.getCreatedBy().getId())
                .orElseThrow(() -> new RuntimeException("User not found with id " + ticket.getCreatedBy().getId()));

        if (creator.getRole() != Role.USER) {
            throw new RuntimeException("Only users with role USER can create tickets");
        }

        ticket.setCreatedBy(creator);
        ticket.setAssignedTo(null); // only admin assigns later
        ticket.setStatus(Status.OPEN);

        return ticketRepository.save(ticket);
    }

    // Assign a ticket to a user (only ADMIN can assign to AGENT)
    @PutMapping("/tickets/{id}/assign/{userId}/by/{adminId}")
    public Ticket assignTicket(@PathVariable("id") int id, @PathVariable("userId") int userId,
            @PathVariable("adminId") int adminId) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id " + id));

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id " + adminId));

        if (admin.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only ADMIN can assign tickets");
        }

        User assignee = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        if (assignee.getRole() != Role.AGENT) {
            throw new RuntimeException("Only AGENT can be assigned tickets");
        }

        ticket.setAssignedTo(assignee);
        ticket.setStatus(Status.IN_PROGRESS);

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
    public List<Ticket> getTicketsByStatus(@PathVariable Status status) {
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

    @PatchMapping("/tickets/{id}")
    public Ticket updateTicketFields(@PathVariable("id") int id, @RequestBody Map<String, Object> updates) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id " + id));

        updates.forEach((field, value) -> {
            switch (field) {
                case "title":
                    ticket.setTitle((String) value);
                    break;
                case "description":
                    ticket.setDescription((String) value);
                    break;
                case "status":
                    // Convert string to enum if your Status is enum
                    ticket.setStatus(Status.valueOf(((String) value).toUpperCase()));
                    break;
                case "priority":
                    // Example if you have Priority enum
                    ticket.setPriority(Priority.valueOf(((String) value).toUpperCase()));
                    break;
                default:
                    throw new RuntimeException("Invalid field: " + field);
            }
        });

        return ticketRepository.save(ticket);
    }

}
