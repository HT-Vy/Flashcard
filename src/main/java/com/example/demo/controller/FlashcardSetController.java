package com.example.demo.controller;



import java.time.LocalDateTime;
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
@RequestMapping("/api/flashcardsets")
public class FlashcardSetController {
    @Autowired
    private FlashcardSetService flashcardSetService;

    @Autowired
    private FlashcardService flashcardService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping // Khi người dùng gửi POST request đến /api/flashcardsets
    public ResponseEntity<?> createFlashcardSet(@RequestBody FlashcardSet flashcardSet,
                                                @RequestHeader("Authorization") String tokenHeader) {
        // Lấy token từ header Authorization (dạng "Bearer xxx") và cắt bỏ phần "Bearer "
        String token = tokenHeader.substring(7); 
        
        // Dùng jwtUtil để giải mã token và lấy ra email của user
        String email = jwtUtil.extractUsername(token);
    
        // Tìm user trong database dựa vào email
        User user = userService.findByEmail(email).orElse(null);
    
        // Nếu không tìm thấy user → trả về lỗi 401 (Unauthorized)
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Người dùng không tồn tại.");
        }
    
        // Gắn thông tin user hiện tại vào flashcardSet (để biết ai là người tạo)
        flashcardSet.setUser(user);
    
        // Gán thời điểm tạo set (hoặc học gần nhất) là thời điểm hiện tại
        flashcardSet.setLastStudiedAt(LocalDateTime.now());
    
        // Gọi service để lưu flashcardSet vào database
        FlashcardSet createdSet = flashcardSetService.createFlashcardSet(flashcardSet);
    
        // Trả về flashcardSet vừa tạo dưới dạng JSON
        return ResponseEntity.ok(createdSet);
    }
    

    @GetMapping // Khi người dùng gửi GET request đến /api/flashcardsets
    public ResponseEntity<?> getMyFlashcardSets(@RequestHeader("Authorization") String tokenHeader) {
        // Lấy token từ header và cắt bỏ phần "Bearer "
        String token = tokenHeader.substring(7);

        // Lấy email từ token
        String email = jwtUtil.extractUsername(token);

        // Tìm user dựa vào email
        User user = userService.findByEmail(email).orElse(null);

        // Nếu không tìm thấy user → trả về lỗi 401
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Người dùng không tồn tại.");
        }

        // Lấy danh sách các flashcardSet mà user này đã tạo
        List<FlashcardSet> sets = flashcardSetService.getFlashcardSetsByUser(user);

        // Trả về danh sách flashcardSet dưới dạng JSON
        return ResponseEntity.ok(sets);
    }

     // ================== 3. Sửa FlashcardSet ==================
     @PutMapping("/{setId}")
     public ResponseEntity<?> updateFlashcardSet(@PathVariable Long setId,
                                                 @RequestBody FlashcardSet updatedSet,
                                                 @RequestHeader("Authorization") String tokenHeader) {
         String token = tokenHeader.substring(7); 
         String email = jwtUtil.extractUsername(token);
         User user = userService.findByEmail(email).orElse(null);
 
         if (user == null) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Người dùng không tồn tại.");
         }
 
         Optional<FlashcardSet> existingSetOpt = flashcardSetService.findBySetId(setId); // Tìm FlashcardSet theo setId
         if (existingSetOpt.isEmpty()) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("FlashcardSet không tồn tại.");
         }
 
         FlashcardSet existingSet = existingSetOpt.get();
         if (!existingSet.getUser().getId().equals(user.getId())) {
             return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền sửa FlashcardSet này.");
         }
 
         // Cập nhật các trường của FlashcardSet
         existingSet.setTitle(updatedSet.getTitle());
         existingSet.setDescription(updatedSet.getDescription());
         existingSet.setIsPublic(updatedSet.getIsPublic());
 
         FlashcardSet savedSet = flashcardSetService.createFlashcardSet(existingSet);
         return ResponseEntity.ok(savedSet);
     }
 
     // ================== 4. Xóa FlashcardSet ==================
     @DeleteMapping("/{setId}")
     public ResponseEntity<?> deleteFlashcardSet(@PathVariable Long setId,
                                                 @RequestHeader("Authorization") String tokenHeader) {
         String token = tokenHeader.substring(7);
         String email = jwtUtil.extractUsername(token);
         User user = userService.findByEmail(email).orElse(null);
 
         if (user == null) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Người dùng không tồn tại.");
         }
 
         Optional<FlashcardSet> existingSetOpt = flashcardSetService.findBySetId(setId); // Tìm FlashcardSet theo setId
         if (existingSetOpt.isEmpty()) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("FlashcardSet không tồn tại.");
         }
 
         FlashcardSet set = existingSetOpt.get();
         if (!set.getUser().getId().equals(user.getId())) {
             return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền xóa FlashcardSet này.");
         }
 
         flashcardSetService.deleteFlashcardSet(setId); // Xóa FlashcardSet
         return ResponseEntity.ok("Xóa thành công.");
     }
 
     // ================== 5. Tìm kiếm FlashcardSet theo tiêu đề ==================
     @GetMapping("/search")
     public ResponseEntity<?> searchFlashcardSets(@RequestParam String keyword) {
         List<FlashcardSet> results = flashcardSetService.searchByTitle(keyword); // Tìm kiếm theo tiêu đề
         return ResponseEntity.ok(results); // Trả về kết quả tìm kiếm
     }
 
    // Thêm flashcard vào bộ flashcardset
    @PostMapping("/{setId}/flashcards")
    public ResponseEntity<?> addFlashcardToSet(@PathVariable Long setId, 
                                               @RequestBody Flashcard flashcard, 
                                               @RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.substring(7); // Lấy token từ header
        String email = jwtUtil.extractUsername(token); // Giải mã token để lấy email của user

        // Tìm người dùng từ email
        User user = userService.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Người dùng không tồn tại.");
        }

        // Tìm FlashcardSet theo setId
        Optional<FlashcardSet> existingSetOpt = flashcardSetService.findBySetId(setId);
        if (existingSetOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("FlashcardSet không tồn tại.");
        }

        FlashcardSet set = existingSetOpt.get();

        // Kiểm tra xem người dùng có phải là chủ sở hữu bộ flashcardSet không
        if (!set.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền thêm flashcard vào FlashcardSet này.");
        }

        // Nếu người dùng có quyền, thêm flashcard vào FlashcardSet
        flashcard.setFlashcardSet(set); // Gắn flashcard vào bộ FlashcardSet
        Flashcard savedFlashcard = flashcardService.createFlashcard(flashcard);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedFlashcard); // Trả về flashcard vừa tạo
    }
 }