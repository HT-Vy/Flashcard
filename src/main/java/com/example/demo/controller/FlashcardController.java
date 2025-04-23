package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Flashcard;
import com.example.demo.model.FlashcardSet;
import com.example.demo.model.User;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.FlashcardService;
import com.example.demo.service.FlashcardSetService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/flashcards")
public class FlashcardController {

    @Autowired
    private FlashcardService flashcardService;

    @Autowired
    private FlashcardSetService flashcardSetService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ Lấy danh sách flashcard của flashcardSet
    @GetMapping("/set/{setId}")
    public ResponseEntity<List<Flashcard>> getFlashcards(@PathVariable Long setId) {
        // Truy vấn các flashcard bằng setId thay vì set.id
        return ResponseEntity.ok(flashcardService.getFlashcardsBySetId(setId));
    }

    // ✅ Sửa flashcard có xác thực quyền
    @PutMapping("/{cardId}")
    public ResponseEntity<?> updateFlashcard(@PathVariable Long cardId,
                                             @RequestBody Flashcard updatedFlashcard,
                                             @RequestHeader("Authorization") String tokenHeader) {
        // Kiểm tra xem flashcard có tồn tại không
        Optional<Flashcard> optionalCard = flashcardService.findById(cardId);
        if (optionalCard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Flashcard existingCard = optionalCard.get();

        // Giải mã JWT để lấy email người dùng
        String token = tokenHeader.substring(7);
        String email = jwtUtil.extractUsername(token);
        User user = userService.findByEmail(email).orElse(null);

        // Kiểm tra quyền sửa flashcard: chỉ người tạo flashcard mới có quyền sửa
        if (user == null || !existingCard.getFlashcardSet().getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Bạn không có quyền sửa flashcard này!");
        }

        // Cập nhật thông tin flashcard
        existingCard.setTerm(updatedFlashcard.getTerm());
        existingCard.setDefinition(updatedFlashcard.getDefinition());
        Flashcard saved = flashcardService.createFlashcard(existingCard);
        return ResponseEntity.ok(saved);
    }

    // ✅ Xóa flashcard có xác thực quyền
    @DeleteMapping("/{cardId}")
    public ResponseEntity<?> deleteFlashcard(@PathVariable Long cardId,
                                             @RequestHeader("Authorization") String tokenHeader) {
        // Kiểm tra xem flashcard có tồn tại không
        Optional<Flashcard> optionalCard = flashcardService.findById(cardId);
        if (optionalCard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Flashcard card = optionalCard.get();

        // Giải mã JWT để lấy email người dùng
        String token = tokenHeader.substring(7);
        String email = jwtUtil.extractUsername(token);
        User user = userService.findByEmail(email).orElse(null);

        // Kiểm tra quyền xóa flashcard: chỉ người tạo flashcard mới có quyền xóa
        if (user == null || !card.getFlashcardSet().getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Bạn không có quyền xóa flashcard này!");
        }

        // Xóa flashcard
        flashcardService.deleteFlashcard(cardId);
        return ResponseEntity.ok("Xóa flashcard thành công!");
    }

    // ✅ Tìm kiếm Flashcard theo term hoặc definition trong FlashcardSet
    @GetMapping("/search/{setId}")
    public ResponseEntity<List<Flashcard>> searchFlashcards(
            @PathVariable Long setId,
            @RequestParam(required = false) String termKeyword,
            @RequestParam(required = false) String definitionKeyword) {
        
        // Tìm FlashcardSet theo setId (giả sử bạn đã có method trong service để lấy FlashcardSet theo setId)
        FlashcardSet set = new FlashcardSet(); // Lấy FlashcardSet từ cơ sở dữ liệu
        // set = flashcardSetService.findById(setId);  // Dùng service để lấy FlashcardSet
        
        // Kiểm tra xem cả hai từ khóa tìm kiếm có rỗng hay không
        if (termKeyword == null && definitionKeyword == null) {
            return ResponseEntity.badRequest().body(null); // Trả về lỗi nếu không có từ khóa tìm kiếm
        }
        
        // Gọi service để tìm kiếm
        List<Flashcard> flashcards = flashcardService.searchFlashcardsByTermOrDefinition(set, termKeyword, definitionKeyword);
        
        // Trả kết quả về cho client
        return ResponseEntity.ok(flashcards);
    }
}