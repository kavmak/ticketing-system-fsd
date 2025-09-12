package com.ticketing.ticketing_system.dto;
import lombok.Data;
import com.ticketing.ticketing_system.enums.Category;
import com.ticketing.ticketing_system.enums.Priority;
import com.ticketing.ticketing_system.enums.Status;

@Data
public class TicketDTO {
    private String title;
    private String description;
    private Category category;
    private Priority priority;
    private Status status;
    private Integer createdByUserId;
    private Integer assignedToUserId;
}
