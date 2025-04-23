package com.example.demo.model;


import java.io.Serializable;


public class UserFlashcardStatusId implements Serializable {
    private Long userId;
    private Long cardId;

    public UserFlashcardStatusId() {}

    public UserFlashcardStatusId(Long userId, Long cardId) {
        this.userId = userId;
        this.cardId = cardId;
    }

    // HashCode & Equals
    @Override
    public int hashCode() {
        return userId.hashCode() + cardId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserFlashcardStatusId that = (UserFlashcardStatusId) obj;
        return userId.equals(that.userId) && cardId.equals(that.cardId);
    }
}
