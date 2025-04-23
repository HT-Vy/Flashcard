package com.example.demo.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "flashcards")
public class Flashcard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    @ManyToOne
    @JoinColumn(name = "set_id", nullable = false)
    private FlashcardSet flashcardSet;

    @Column(nullable = false, length = 50)
    private String term;

    @Column(nullable = false, length = 50)
    private String definition;

    // Getters, Setters, Constructors
}

