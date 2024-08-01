package com.sokpulee.crescendo.domain.quiz.service;

import com.sokpulee.crescendo.domain.quiz.dto.request.QuizCreateRequest;

public interface QuizService {
    void createQuiz(Long loggedInUserId, QuizCreateRequest quizCreateRequest);
}
