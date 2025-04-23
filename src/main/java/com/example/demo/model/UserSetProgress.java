package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_set_progress")
@IdClass(UserSetProgressId.class)
public class UserSetProgress {
    @Id
    private Long userId;

    @Id
    private Long setId;

    //Sắp xếp theo thời gian học gần nhất
    @Column(nullable = false)
    private LocalDateTime lastStudiedAt;

    //Lưu thẻ cuối cùng mà user học trong bộ thẻ
    @Column
    private Long lastStudiedCardId;
}