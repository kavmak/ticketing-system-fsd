package com.ticketing.ticketing_system.mappers;

import com.ticketing.ticketing_system.dto.UserDTO;
import com.ticketing.ticketing_system.entities.User;

public class UserMapper {
    public static UserDTO toDTO(User u) {
        if (u == null) {
            return null;
        }

        return new UserDTO(
                u.getId(),
                u.getName(),
                u.getEmail(),
                u.getPassword(),
                u.getRole()
        );
    }
}
