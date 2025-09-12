package com.ticketing.ticketing_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.ticketing.ticketing_system.enums.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO{
   private int id;
   private String name;
   private String email;
   private String password;
   private Role role;

}