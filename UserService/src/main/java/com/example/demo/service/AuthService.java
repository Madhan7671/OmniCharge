package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.RegisterRequestDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private JwtUtil jwtUtil;

    // REGISTER
    public void register(RegisterRequestDTO request) {

        if (repo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        if (repo.findByUserName(request.getUserName()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if (repo.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new RuntimeException("Phone number already exists");
        }

        User user = new User();
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setCountryCode(request.getCountryCode());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(request.getPassword()); // will encrypt later
        user.setRole("USER");

        repo.save(user);
    }

    // LOGIN
    public String login(LoginRequestDTO request) {

        User user = repo.findByEmailOrUserName(
                request.getIdentifier(),
                request.getIdentifier()
        ).orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(user.getEmail(), user.getRole());
    }
    public void assignRole(String email, String role) {

        User user = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(role);
        repo.save(user);
    }
}
