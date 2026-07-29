package com.complaint.management.dto;

import com.complaint.management.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private Role role;
    private String companyCode;
}