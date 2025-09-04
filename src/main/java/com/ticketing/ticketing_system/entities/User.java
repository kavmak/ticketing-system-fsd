package com.ticketing.ticketing_system.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import com.ticketing.ticketing_system.enums.Role;
import lombok.Data;


@Data
@Entity 
public class User {
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)

  int userId;
  String email;
  String password;

  @Enumerated(EnumType.STRING)
  Role role;
    
}
