package com.sokpulee.crescendo.domain.quiz.repository;

import com.sokpulee.crescendo.domain.quiz.entity.QuizQuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface QuizQuestionAnswerRepository extends JpaRepository<QuizQuestionAnswer, Long> {
}
