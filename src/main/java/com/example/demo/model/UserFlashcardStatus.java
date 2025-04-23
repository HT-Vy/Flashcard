package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_flashcard_status")
@IdClass(UserFlashcardStatusId.class)
public class UserFlashcardStatus {
    @Id
    private Long userId;

    @Id
    private Long cardId;

    @Column(nullable = false, length = 50)
    private String status; // "đã học" hoặc "chưa học"

    // Getters, Setters, Constructors
}
