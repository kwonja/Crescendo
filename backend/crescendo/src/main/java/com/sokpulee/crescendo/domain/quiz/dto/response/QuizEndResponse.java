package com.sokpulee.crescendo.domain.quiz.dto.response;

import lombok.Getter;

@Getter
public class QuizEndResponse {

    private int rankPercentage;

    public QuizEndResponse(int rankPercentage) {
        this.rankPercentage = rankPercentage;
    }
}
