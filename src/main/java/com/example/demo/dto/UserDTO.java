package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
public class UserDTO {
    private String userName;
    private String email;

    public UserDTO(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    // Getters, Setters
}

