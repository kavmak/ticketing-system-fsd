package com.ticketing.ticketing_system.mappers;

import com.ticketing.ticketing_system.dto.TicketDTO;
import com.ticketing.ticketing_system.entities.Ticket;
import com.ticketing.ticketing_system.entities.User;

public class TicketMapper {
    public static Ticket toEntity(TicketDTO dto, User creator, User assignee){
        Ticket ticket = new Ticket();
        ticket .setTitle(dto.getTitle());
        ticket.setDescription(dto.getDescription());
        ticket.setCategory(dto.getCategory());
        ticket.setPriority(dto.getPriority());
        ticket.setStatus(dto.getStatus());

        ticket.setCreatedBy(creator);
        ticket.setAssignedTo(assignee);

        return ticket;
    }

      public static TicketDTO toDTO(Ticket ticket) {
        TicketDTO dto = new TicketDTO();
        dto.setTitle(ticket.getTitle());
        dto.setDescription(ticket.getDescription());
        dto.setCategory(ticket.getCategory());
        dto.setPriority(ticket.getPriority());
        dto.setStatus(ticket.getStatus());

        dto.setCreatedByUserId(ticket.getCreatedByUserId());
        dto.setAssignedToUserId(ticket.getAssignedToUserId());

        return dto;
    }
}
