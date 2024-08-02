package com.sokpulee.crescendo.domain.quiz.controller;

import com.sokpulee.crescendo.domain.quiz.dto.request.QuizCreateRequest;
import com.sokpulee.crescendo.domain.quiz.dto.request.QuizEndRequest;
import com.sokpulee.crescendo.domain.quiz.dto.response.QuizEndResponse;
import com.sokpulee.crescendo.domain.quiz.dto.response.QuizStartResponse;
import com.sokpulee.crescendo.domain.quiz.service.QuizService;
import com.sokpulee.crescendo.global.auth.annotation.AuthPrincipal;
import com.sokpulee.crescendo.global.exception.custom.AuthenticationRequiredException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/quiz")
@Tag(name = "Quiz", description = "퀴즈 관련 API")
public class QuizController {

    private final QuizService quizService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "퀴즈 생성", description = "퀴즈 생성 API")
    public ResponseEntity<?> createQuiz(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @Valid @ModelAttribute QuizCreateRequest quizCreateRequest
    ) {

        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        quizService.createQuiz(loggedInUserId, quizCreateRequest);
        return ResponseEntity.status(OK).build();
    }

    @GetMapping("/{quiz-id}/start")
    @Operation(summary = "퀴즈 시작", description = "퀴즈 시작 API")
    public ResponseEntity<?> startQuiz(@PathVariable("quiz-id") Long quizId) {

        QuizStartResponse response = quizService.startQuiz(quizId);
        return ResponseEntity.status(OK).body(response);
    }

    @PostMapping("/{quiz-id}/end")
    @Operation(summary = "퀴즈 종료", description = "퀴즈 종료 API")
    public ResponseEntity<?> endQuiz(
            @PathVariable("quiz-id") Long quizId,
            @RequestBody QuizEndRequest quizEndRequestDTO) {
        QuizEndResponse response = quizService.endQuiz(quizId, quizEndRequestDTO);
        return ResponseEntity.status(OK).body(response);
    }
}
