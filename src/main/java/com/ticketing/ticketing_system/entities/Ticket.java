package com.ticketing.ticketing_system.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
     
    int userId;

    String title;
    String status;
    String description;
    String priority;
    String category;
    String assignedTo;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime closedAt;

    //Getters and Setters

    //id
    public int getId(){
        return id;
    }

    public void setId(int id){
         this.id=id;
    }

    //userid
    public int getUserId(){
        return userId;
    }

    public void setUserId(int userId){
        this.userId=userId;
    }
    
    //title
      public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    //status
     public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    //description
     public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    // priority
public String getPriority() {
    return priority;
}

public void setPriority(String priority) {
    this.priority = priority;
}

// category
public String getCategory() {
    return category;
}

public void setCategory(String category) {
    this.category = category;
}


    //assigned to
     public String getAssignedTo() {
        return assignedTo;
    }
    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }


        // createdAt
    public LocalDateTime getCreatedAt() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now(); 
        }
        return createdAt;
    }

    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now(); 
    }

    //updated at
     public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

    //closed at
     public LocalDateTime getClosedAt() {
        return closedAt;
    }
    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }


}



