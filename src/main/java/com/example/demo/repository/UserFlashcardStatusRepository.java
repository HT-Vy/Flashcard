package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.UserFlashcardStatus;
import com.example.demo.model.UserFlashcardStatusId;

@Repository
public interface UserFlashcardStatusRepository extends JpaRepository<UserFlashcardStatus, UserFlashcardStatusId> {
    List<UserFlashcardStatus> findByUserId(Long userId);
    List<UserFlashcardStatus> findByUserIdAndStatus(Long userId, String status);
}

