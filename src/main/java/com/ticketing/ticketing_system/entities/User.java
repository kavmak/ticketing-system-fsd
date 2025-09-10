package com.ticketing.ticketing_system.entities;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ticketing.ticketing_system.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; // USER, AGENT, ADMIN

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
   

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    private List<Ticket> ticketsCreated;

    @OneToMany(mappedBy = "assignedTo")
    @JsonIgnore
    private List<Ticket> ticketsAssigned;

    public User() {}

    // Conditionally include tickets based on role
    @JsonProperty("tickets")
    public List<Ticket> getTicketsForRole() {
        if (role == Role.USER) {
            return ticketsCreated;
        } else if (role == Role.AGENT) {
            return ticketsAssigned;
        }
        return null; // ADMIN → no tickets
    }
}
