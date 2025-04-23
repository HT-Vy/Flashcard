package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
public class AuthResponse {
    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }

    // Getters, Setters
}

