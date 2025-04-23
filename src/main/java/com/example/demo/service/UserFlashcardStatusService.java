package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.UserFlashcardStatus;
import com.example.demo.repository.UserFlashcardStatusRepository;

@Service
public class UserFlashcardStatusService {
    @Autowired
    private UserFlashcardStatusRepository statusRepository;

    public void updateFlashcardStatus(Long userId, Long cardId, String status) {
        UserFlashcardStatus flashcardStatus = new UserFlashcardStatus(userId, cardId, status);
        statusRepository.save(flashcardStatus);
    }

    public List<UserFlashcardStatus> getFlashcardStatusByUser(Long userId) {
        return statusRepository.findByUserId(userId);
    }

    public List<UserFlashcardStatus> getFlashcardsByStatus(Long userId, String status) {
        return statusRepository.findByUserIdAndStatus(userId, status);
    }
}

