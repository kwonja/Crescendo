package com.sokpulee.crescendo.domain.quiz.repository;

import com.sokpulee.crescendo.domain.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    @Query("SELECT q FROM Quiz q " +
            "LEFT JOIN FETCH q.questions qq " +
            "LEFT JOIN FETCH qq.questionAnswers " +
            "WHERE q.id = :id")
    Optional<Quiz> findQuizWithDetailsById(@Param("id") Long id);
}
