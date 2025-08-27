package com.example.company.dto;

import com.example.company.model.Role;
import lombok.Data;

@Data
public class SignUpRequest {
    private String username;
    private String password;
    private Role role;
}