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

import com.example.demo.model.UserSetProgress;
import com.example.demo.service.UserSetProgressService;

@RestController
@RequestMapping("/api/user-progress")
public class UserSetProgressController {
    @Autowired
    private UserSetProgressService progressService;

    @PostMapping("/update")
    public ResponseEntity<?> updateProgress(@RequestParam Long userId, @RequestParam Long setId, @RequestParam Long cardId) {
        progressService.updateProgress(userId, setId, cardId);
        return ResponseEntity.ok("Tiến độ học đã được cập nhật.");
    }

    @GetMapping("/recent/{userId}")
    public ResponseEntity<List<UserSetProgress>> getRecentSets(@PathVariable Long userId) {
        return ResponseEntity.ok(progressService.getRecentStudiedSets(userId));
    }
}

