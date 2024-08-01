package com.sokpulee.crescendo.domain.quiz.repository;

import com.sokpulee.crescendo.domain.quiz.entity.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {
}
