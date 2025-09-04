package com.ticketing.ticketing_system.controllers;


import com.ticketing.ticketing_system.entities.Ticket;
import com.ticketing.ticketing_system.repositories.TicketRepository;
import java.util.List;
import java.util.Optional;


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

@RestController
public class TicketController {
    
    @Autowired
    TicketRepository ticketRepository;

    @GetMapping("/test")
    public String test(){
        return "test done";
    }
    
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
    @ResponseStatus(HttpStatus.CREATED)
    public Ticket addTicket(@RequestBody Ticket ticket) {
        return ticketRepository.save(ticket);
    }

        @PutMapping("/tickets/{id}")
    public Ticket updateTicket(@PathVariable("id") int id, @RequestBody Ticket ticketDetails) {
        return ticketRepository.findById(id).map(ticket -> {
            ticket.setTitle(ticketDetails.getTitle());
            ticket.setStatus(ticketDetails.getStatus());
            ticket.setDescription(ticketDetails.getDescription());
            ticket.setAssignedTo(ticketDetails.getAssignedTo());
           // ticket.setCreatedAt(ticketDetails.getCreatedAt());
           // ticket.setUpdatedAt(ticketDetails.getUpdatedAt());
           // ticket.setClosedAt(ticketDetails.getClosedAt());
            ticket.setUserId(ticketDetails.getUserId());
            return ticketRepository.save(ticket);
        }).orElseThrow(() -> new TicketNotFoundException("Ticket not found with id " + id));
    }
  

     @DeleteMapping("/tickets/{id}")
    public void deleteTicket(@PathVariable("id") int id) {
        try {
            ticketRepository.deleteById(id);
        } catch(Exception e) {
            e.printStackTrace();
        }
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

}



 
