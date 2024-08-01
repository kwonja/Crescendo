package com.sokpulee.crescendo.domain.quiz.repository;

import com.sokpulee.crescendo.domain.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
