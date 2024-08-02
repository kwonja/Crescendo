package com.sokpulee.crescendo.domain.quiz.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuizEndRequest {

    @NotNull(message = "맞은 갯수는 필수값입니다.")
    private Integer correctAnswerCount;

    public QuizEndRequest(Integer correctAnswerCount) {
        this.correctAnswerCount = correctAnswerCount;
    }
}
