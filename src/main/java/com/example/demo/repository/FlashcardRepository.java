package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Flashcard;
import com.example.demo.model.FlashcardSet;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {
    List<Flashcard> findByFlashcardSet(FlashcardSet set); // Tìm kiếm theo FlashcardSet
    List<Flashcard> findByFlashcardSetSetId(Long setId); // Sử dụng setId thay vì id
    Optional<Flashcard> findById(Long id); // Tìm kiếm flashcard theo id
    void deleteById(Long id); // Xóa flashcard theo id
    // Sửa lại phương thức tìm kiếm flashcard theo term hoặc definition trong flashcardset
    List<Flashcard> findByFlashcardSetAndTermContainingOrFlashcardSetAndDefinitionContaining(
            FlashcardSet flashcardSet, String termKeyword, FlashcardSet flashcardSet2, String definitionKeyword);
}

