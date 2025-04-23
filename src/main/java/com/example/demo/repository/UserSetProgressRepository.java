package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.UserSetProgress;
import com.example.demo.model.UserSetProgressId;

@Repository
public interface UserSetProgressRepository extends JpaRepository<UserSetProgress, UserSetProgressId> {
    List<UserSetProgress> findByUserIdOrderByLastStudiedAtDesc(Long userId);
}

