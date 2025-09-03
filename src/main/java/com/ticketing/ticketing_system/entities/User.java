package com.ticketing.ticketing_system.entities;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; // CUSTOMER, AGENT, ADMIN

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    // One user can create many tickets
    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    private List<Ticket> ticketsCreated;


    // One user (agent) can be assigned many tickets
    @OneToMany(mappedBy = "assignedTo")
    @JsonIgnore
    private List<Ticket> ticketsAssigned;

    // ---------------- Getters & Setters ----------------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<Ticket> getTicketsCreated() { return ticketsCreated; }
    public void setTicketsCreated(List<Ticket> ticketsCreated) { this.ticketsCreated = ticketsCreated; }

    public List<Ticket> getTicketsAssigned() { return ticketsAssigned; }
    public void setTicketsAssigned(List<Ticket> ticketsAssigned) { this.ticketsAssigned = ticketsAssigned; }
}
