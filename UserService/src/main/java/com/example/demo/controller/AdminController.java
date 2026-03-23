package com.example.demo.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AuthService service;

    // 🔥 1. Get all users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    // 🔥 2. Delete user
    @DeleteMapping("/delete-user")
    public String deleteUser(@RequestParam Long id) {
        userRepo.deleteById(id);
        return "User deleted";
    }

    // 🔥 3. Assign role
    @PostMapping("/assign-role")
    public String assignRole(@RequestParam String email,
                             @RequestParam String role) {

        service.assignRole(email, role);
        return "Role updated";
    }
}
