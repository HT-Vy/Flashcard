package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.FlashcardSet;
import com.example.demo.model.User;

@Repository
public interface FlashcardSetRepository extends JpaRepository<FlashcardSet, Long> {
    // Tìm kiếm FlashcardSet theo người dùng (user)
    List<FlashcardSet> findByUser(User user);

    // Tìm kiếm FlashcardSet theo tiêu đề, hỗ trợ tìm kiếm không phân biệt hoa thường
    List<FlashcardSet> findByTitleContainingIgnoreCase(String keyword);

    // Tìm kiếm FlashcardSet theo setId (thay vì findById(), dùng findBySetId())
    Optional<FlashcardSet> findBySetId(Long setId);
}

