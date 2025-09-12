package com.ticketing.ticketing_system.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;


import com.ticketing.ticketing_system.entities.User;
import com.ticketing.ticketing_system.enums.Role;
import com.ticketing.ticketing_system.mappers.UserMapper;
import com.ticketing.ticketing_system.repositories.UserRepository;
import com.ticketing.ticketing_system.dto.UserDTO;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Create a new user
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // Get all users
    @Cacheable("users")
    @GetMapping
    public List<User> getAllUsers() {
        // tickets will be included based on role (handled in User.java)
        return userRepository.findAll();
    }

    // Get users by role
    @GetMapping("/role/{role}")
    public List<User> getUsersByRole(@PathVariable Role role) {
        return userRepository.findByRole(role);
    }

    // Get user by ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Update user
    @PutMapping("/{id}")
    public User updateUser(@PathVariable("id") int id, @RequestBody User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(updatedUser.getName());
                    user.setEmail(updatedUser.getEmail());
                    user.setPassword(updatedUser.getPassword());
                    user.setRole(updatedUser.getRole());
                    user.setUpdatedAt(updatedUser.getUpdatedAt());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Delete user
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userRepository.deleteById(id);
        return "User deleted successfully";
    }

    //get all users without ticket mapping
    @GetMapping("/without-tickets")
    public List<UserDTO> getAllUsersWithoutTickets(){
        return userRepository.findAll()
                             .stream()
                             .map(UserMapper::toDTO)
                             .toList();
    }

}
