package com.example.demo.model;

import java.io.Serializable;

public class UserSetProgressId implements Serializable {
    private Long userId;
    private Long setId;

    public UserSetProgressId() {}

    public UserSetProgressId(Long userId, Long setId) {
        this.userId = userId;
        this.setId = setId;
    }

    // HashCode & Equals
    @Override
    public int hashCode() {
        return userId.hashCode() + setId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserSetProgressId that = (UserSetProgressId) obj;
        return userId.equals(that.userId) && setId.equals(that.setId);
    }
}

