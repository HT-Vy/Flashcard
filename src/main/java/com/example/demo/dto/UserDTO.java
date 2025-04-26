package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
public class UserDTO {
    private String fullName;
    private String email;

    public UserDTO(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }

    // Getters, Setters
}

