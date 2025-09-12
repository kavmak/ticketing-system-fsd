package com.ticketing.ticketing_system.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ticketing.ticketing_system.enums.Category;
import com.ticketing.ticketing_system.enums.Priority;
import com.ticketing.ticketing_system.enums.Status;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String title;
    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime closedAt;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @ManyToOne


    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference("createdBy")
    private User createdBy;

    @ManyToOne


    @JoinColumn(name = "assigned_to")
    @JsonBackReference("assignedTo")
    private User assignedTo;

    // Expose createdBy as just ID
    @JsonProperty("createdByUserId")
    public Integer getCreatedByUserId() {
        return createdBy != null ? createdBy.getId() : null;
    }

    // Expose assignedTo as just ID
    @JsonProperty("assignedToUserId")
    public Integer getAssignedToUserId() {
        return assignedTo != null ? assignedTo.getId() : null;
    }
}
