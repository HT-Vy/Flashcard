package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.UserFlashcardStatus;
import com.example.demo.service.UserFlashcardStatusService;

@RestController
@RequestMapping("/api/flashcard-status")
public class UserFlashcardStatusController {
    @Autowired
    private UserFlashcardStatusService statusService;

    @PostMapping("/update/{userId}/{cardId}")
    public ResponseEntity<?> updateStatus(@PathVariable Long userId, @PathVariable Long cardId, @RequestParam String status) {
        
        if (!status.equals("đã học") && !status.equals("chưa học")) {
            return ResponseEntity.badRequest().body("Trạng thái không hợp lệ!");
        }

        statusService.updateFlashcardStatus(userId, cardId, status);
        return ResponseEntity.ok("Trạng thái thẻ đã được cập nhật.");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserFlashcardStatus>> getUserFlashcardStatus(@PathVariable Long userId) {
        return ResponseEntity.ok(statusService.getFlashcardStatusByUser(userId));
    }

    @GetMapping("/{userId}/filter")
    public ResponseEntity<List<UserFlashcardStatus>> getFlashcardsByStatus(
            @PathVariable Long userId,
            @RequestParam String status) {
        return ResponseEntity.ok(statusService.getFlashcardsByStatus(userId, status));
    }
}

