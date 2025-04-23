package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Flashcard;
import com.example.demo.model.FlashcardSet;
import com.example.demo.service.QuizResultService;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {
    @Autowired
    private QuizResultService quizService;

    @GetMapping("/generate")
    public ResponseEntity<List<Flashcard>> generateQuiz(
            @RequestBody FlashcardSet set,
            @RequestParam(defaultValue = "5") int numberOfQuestions) {
        return ResponseEntity.ok(quizService.generateQuiz(set, numberOfQuestions));
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitQuiz(
            @RequestParam Long userId,
            @RequestParam Long setId,
            @RequestParam int score,
            @RequestParam int totalQuestions) {
        quizService.saveQuizResult(userId, setId, score, totalQuestions);
        return ResponseEntity.ok("Quiz submitted successfully!");
    }
}
