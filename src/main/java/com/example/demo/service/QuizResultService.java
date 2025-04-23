package com.example.demo.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Flashcard;
import com.example.demo.model.QuizResult;
import com.example.demo.model.User;
import com.example.demo.model.FlashcardSet;
import com.example.demo.repository.FlashcardRepository;
import com.example.demo.repository.FlashcardSetRepository;
import com.example.demo.repository.QuizResultRepository;
import com.example.demo.repository.UserRepository;

@Service
public class QuizResultService {
    @Autowired
    private FlashcardRepository flashcardRepository;

    @Autowired
    private QuizResultRepository quizResultRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FlashcardSetRepository flashcardSetRepository;

    public List<Flashcard> generateQuiz(FlashcardSet set, int numberOfQuestions) {
        List<Flashcard> flashcards = flashcardRepository.findByFlashcardSet(set);
        Collections.shuffle(flashcards); // Trộn ngẫu nhiên
        return flashcards.stream().limit(numberOfQuestions).collect(Collectors.toList());
    }

    //Lưu kq của user
    public QuizResult saveQuizResult(Long userId, Long setId, int score, int totalQuestions) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    
        FlashcardSet flashcardSet = flashcardSetRepository.findById(setId)
            .orElseThrow(() -> new RuntimeException("FlashcardSet not found"));
        QuizResult result = new QuizResult();
        result.setUser(user);
        result.setFlashcardSet(flashcardSet);
        result.setScore(score);
        result.setTotalQuestions(totalQuestions);

        return quizResultRepository.save(result);
    }
}

