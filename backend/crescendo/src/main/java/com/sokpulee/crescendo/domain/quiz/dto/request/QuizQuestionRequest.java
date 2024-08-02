package com.sokpulee.crescendo.domain.quiz.dto.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class QuizQuestionRequest {
    private MultipartFile quizImage;
    private List<String> answer;

    public QuizQuestionRequest(MultipartFile quizImage, List<String> answer) {
        this.quizImage = quizImage;
        this.answer = answer;
    }
}
