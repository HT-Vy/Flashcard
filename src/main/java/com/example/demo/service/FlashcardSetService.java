package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Flashcard;
import com.example.demo.model.FlashcardSet;
import com.example.demo.model.User;
import com.example.demo.repository.FlashcardRepository;
import com.example.demo.repository.FlashcardSetRepository;

@Service
public class FlashcardSetService {
    @Autowired
    private FlashcardSetRepository flashcardSetRepository;

     @Autowired
    private FlashcardRepository flashcardRepository;

    // Tạo mới một flashcard set
    public FlashcardSet createFlashcardSet(FlashcardSet set) {
        return flashcardSetRepository.save(set);
    }

    // Lấy flashcard set theo setId (sửa lại theo quy tắc findBySetId)
    public Optional<FlashcardSet> findBySetId(Long setId) {
        return flashcardSetRepository.findBySetId(setId);
    }

    // Lấy danh sách các flashcard set của một user
    public List<FlashcardSet> getFlashcardSetsByUser(User user) {
        return flashcardSetRepository.findByUser(user);
    }

    // Xóa flashcard set theo setId
    public void deleteFlashcardSet(Long setId) {
        flashcardSetRepository.deleteById(setId);
    }

    // Tìm kiếm flashcard set theo tiêu đề (keyword)
    public List<FlashcardSet> searchByTitle(String keyword) {
        return flashcardSetRepository.findByTitleContainingIgnoreCase(keyword);
    }

    // Thêm flashcard vào bộ flashcardset
    // public Flashcard addFlashcardToSet(Long setId, Flashcard flashcard) {
    //     FlashcardSet flashcardSet = flashcardSetRepository.findBySetId(setId)
    //             .orElseThrow(() -> new RuntimeException("Flashcard Set not found"));
    //     flashcard.setFlashcardSet(flashcardSet);
    //     return flashcardRepository.save(flashcard);
    // }
    // Thêm flashcard vào bộ flashcardset (hoặc tạo mới FlashcardSet nếu không tồn tại)
    public Flashcard addFlashcardToSet(Long setId, Flashcard flashcard) {
        // Tìm FlashcardSet theo setId
        FlashcardSet flashcardSet = flashcardSetRepository.findBySetId(setId)
                .orElseGet(() -> {
                    // Nếu FlashcardSet không tồn tại, tạo một FlashcardSet mới
                    FlashcardSet newFlashcardSet = new FlashcardSet();
                    // Thiết lập các giá trị cho FlashcardSet mới (ví dụ: setName, user, v.v.)
                    // Bạn có thể thiết lập các thuộc tính của FlashcardSet ở đây nếu cần.
                    flashcardSetRepository.save(newFlashcardSet); // Lưu FlashcardSet mới vào DB
                    return newFlashcardSet; // Trả về FlashcardSet mới để sử dụng
                });

        // Gán FlashcardSet cho Flashcard
        flashcard.setFlashcardSet(flashcardSet);

        // Lưu Flashcard vào cơ sở dữ liệu (nếu FlashcardSet mới thì Flashcard cũng sẽ được lưu)
        return flashcardRepository.save(flashcard);
    }

}

