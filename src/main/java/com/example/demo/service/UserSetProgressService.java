package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.UserSetProgress;
import com.example.demo.model.UserSetProgressId;
import com.example.demo.repository.UserSetProgressRepository;

@Service
public class UserSetProgressService {
    @Autowired
    private UserSetProgressRepository progressRepository;

    public void updateProgress(Long userId, Long setId, Long lastStudiedCardId) {
        UserSetProgress progress = progressRepository.findById(new UserSetProgressId(userId, setId))
                .orElse(new UserSetProgress(userId, setId, LocalDateTime.now(), lastStudiedCardId));

        progress.setLastStudiedAt(LocalDateTime.now());
        progress.setLastStudiedCardId(lastStudiedCardId);

        progressRepository.save(progress);
    }

    public List<UserSetProgress> getRecentStudiedSets(Long userId) {
        return progressRepository.findByUserIdOrderByLastStudiedAtDesc(userId);
    }
}
