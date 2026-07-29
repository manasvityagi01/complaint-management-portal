package com.complaint.management.service;

import com.complaint.management.model.Role;
import com.complaint.management.model.User;
import com.complaint.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists!");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }
        if (user.getRole() == null) {
            user.setRole(Role.CUSTOMER);
        }
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public List<User> getActiveSupportAgents() {
        return userRepository.findByRoleAndIsActive(Role.SUPPORT_AGENT, true);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deactivateUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User u = user.get();
            u.setIsActive(false);
            userRepository.save(u);
        }
    }

    public Optional<User> authenticate(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password) && user.get().getIsActive()) {
            return user;
        }
        return Optional.empty();
    }
}