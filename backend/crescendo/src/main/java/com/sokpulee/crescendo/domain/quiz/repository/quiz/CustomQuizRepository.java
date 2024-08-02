package com.sokpulee.crescendo.domain.quiz.repository.quiz;

import com.sokpulee.crescendo.domain.quiz.dto.response.QuizListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomQuizRepository {
    Page<QuizListResponse> findQuizzes(String title, Pageable pageable);
}
