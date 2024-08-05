package com.sokpulee.crescendo.domain.quiz.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class QuizQuestionDTO {
    private String quizImagePath;
    private List<String> answer;

    public QuizQuestionDTO(String quizImagePath, List<String> answer) {
        this.quizImagePath = quizImagePath;
        this.answer = answer;
    }
}
