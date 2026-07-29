package com.complaint.management.controller;

import com.complaint.management.dto.ApiResponse;
import com.complaint.management.dto.LoginRequest;
import com.complaint.management.dto.UserRegistrationRequest;
import com.complaint.management.model.User;
import com.complaint.management.model.Role;
import com.complaint.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    // Register new user
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@RequestBody UserRegistrationRequest request) {
        try {
            if (request.getRole() == Role.ADMIN) {
                // Yahan humne company code database jaisa fix kar diya hai
                String secretCode = "mana2027";
                if (request.getCompanyCode() == null || !request.getCompanyCode().equals(secretCode)) {
                    return ResponseEntity.badRequest().body(ApiResponse.error("Invalid Company Code! You cannot register as Admin."));
                }
            }
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword());
            user.setFullName(request.getFullName());
            user.setEmail(request.getEmail());
            user.setPhone(request.getPhone());
            user.setRole(request.getRole());

            User savedUser = userService.registerUser(user);
            return ResponseEntity.ok(ApiResponse.success("User registered successfully!", savedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<User>> login(@RequestBody LoginRequest request) {
        Optional<User> user = userService.authenticate(request.getUsername(), request.getPassword());

        if (user.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("Login successful!", user.get()));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid username or password!"));
        }
    }

    // Get all users
    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);

        if (user.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(user.get()));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error("User not found!"));
        }
    }

    // Get all support agents
    @GetMapping("/agents")
    public ResponseEntity<ApiResponse<List<User>>> getAgents() {
        List<User> agents = userService.getActiveSupportAgents();
        return ResponseEntity.ok(ApiResponse.success(agents));
    }
}