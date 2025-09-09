package com.ticketing.ticketing_system.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ticketing.ticketing_system.entities.User;
import com.ticketing.ticketing_system.enums.Role;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
}
