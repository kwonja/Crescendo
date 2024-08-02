package com.sokpulee.crescendo.domain.quiz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class QuizListResponse {
    private String thumbnailPath;
    private String title;
    private String content;

    public QuizListResponse(String thumbnailPath, String title, String content) {
        this.thumbnailPath = thumbnailPath;
        this.title = title;
        this.content = content;
    }
}
