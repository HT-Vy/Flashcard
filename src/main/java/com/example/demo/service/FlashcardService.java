package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Flashcard;
import com.example.demo.model.FlashcardSet;
import com.example.demo.repository.FlashcardRepository;

@Service
public class FlashcardService {

    @Autowired
    private FlashcardRepository flashcardRepository;

    // ✅ Tạo hoặc cập nhật một flashcard
    public Flashcard createFlashcard(Flashcard flashcard) {
        // Nếu flashcard có id thì sẽ cập nhật, nếu không có id thì tạo mới
        return flashcardRepository.save(flashcard);
    }

    // ✅ Lấy danh sách flashcard theo FlashcardSet
    public List<Flashcard> getBySet(FlashcardSet set) {
        // Dùng để hiển thị các flashcard thuộc một bộ flashcard cụ thể
        return flashcardRepository.findByFlashcardSet(set);
    }

    // ✅ Lấy danh sách flashcard theo setId (dùng trong controller)
    public List<Flashcard> getFlashcardsBySetId(Long setId) {
        // Dùng hàm custom truy vấn theo flashcardSet.setId thay vì flashcardSet.id
        return flashcardRepository.findByFlashcardSetSetId(setId);
    }

    // ✅ Tìm flashcard theo id (dùng khi cần kiểm tra flashcard thuộc user nào)
    public Optional<Flashcard> findById(Long id) {
        return flashcardRepository.findById(id);
    }

    // ✅ Xóa flashcard theo id
    public void deleteFlashcard(Long cardId) {
        flashcardRepository.deleteById(cardId);
    }

    // ✅ Tìm kiếm Flashcard theo term hoặc definition trong FlashcardSet
    // Tìm kiếm Flashcard theo term hoặc definition trong FlashcardSet
    public List<Flashcard> searchFlashcardsByTermOrDefinition(FlashcardSet set, String termKeyword, String definitionKeyword) {
        return flashcardRepository.findByFlashcardSetAndTermContainingOrFlashcardSetAndDefinitionContaining(
                set, termKeyword, set, definitionKeyword);
    }
}

