package com.sokpulee.crescendo.domain.quiz.service;

import com.sokpulee.crescendo.domain.quiz.dto.request.QuizCreateRequest;
import com.sokpulee.crescendo.domain.quiz.dto.request.QuizEndRequest;
import com.sokpulee.crescendo.domain.quiz.dto.response.QuizEndResponse;
import com.sokpulee.crescendo.domain.quiz.dto.response.QuizStartResponse;

public interface QuizService {
    void createQuiz(Long loggedInUserId, QuizCreateRequest quizCreateRequest);

    QuizStartResponse startQuiz(Long quizId);

    QuizEndResponse endQuiz(Long quizId, QuizEndRequest quizEndRequest);
}
