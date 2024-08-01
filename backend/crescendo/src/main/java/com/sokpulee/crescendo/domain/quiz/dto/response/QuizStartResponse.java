package com.sokpulee.crescendo.domain.quiz.dto.response;

import com.sokpulee.crescendo.domain.quiz.dto.QuizQuestionDTO;
import lombok.Getter;

import java.util.List;

@Getter
public class QuizStartResponse {

    private List<QuizQuestionDTO> questionList;

    public QuizStartResponse(List<QuizQuestionDTO> questionList) {
        this.questionList = questionList;
    }
}
