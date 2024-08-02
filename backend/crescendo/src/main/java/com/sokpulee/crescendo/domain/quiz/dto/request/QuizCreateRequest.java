package com.sokpulee.crescendo.domain.quiz.dto.request;

import com.sokpulee.crescendo.domain.quiz.dto.QuizQuestionDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class QuizCreateRequest {

    @NotBlank(message = "제목은 필수 값 입니다.")
    private String title;

    @NotBlank(message = "본문은 필수 값 입니다.")
    private String content;

    @NotNull(message = "문제 개수는 필수 값입니다.")
    private Integer questionNum;

    @NotNull(message = "썸네일은 필수 값 입니다.")
    private MultipartFile thumbnail;

    @NotNull(message = "퀴즈 문제들은 필수 값 입니다.")
    private List<QuizQuestionRequest> questionList;

    public QuizCreateRequest(String title, String content, Integer questionNum, MultipartFile thumbnail, List<QuizQuestionRequest> questionList) {
        this.title = title;
        this.content = content;
        this.questionNum = questionNum;
        this.thumbnail = thumbnail;
        this.questionList = questionList;
    }
}
