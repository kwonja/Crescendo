package com.sokpulee.crescendo.domain.quiz.service;

import com.sokpulee.crescendo.domain.quiz.dto.QuizQuestionDTO;
import com.sokpulee.crescendo.domain.quiz.dto.request.QuizCreateRequest;
import com.sokpulee.crescendo.domain.quiz.dto.request.QuizEndRequest;
import com.sokpulee.crescendo.domain.quiz.dto.request.QuizQuestionRequest;
import com.sokpulee.crescendo.domain.quiz.dto.response.QuizEndResponse;
import com.sokpulee.crescendo.domain.quiz.dto.response.QuizListResponse;
import com.sokpulee.crescendo.domain.quiz.dto.response.QuizStartResponse;
import com.sokpulee.crescendo.domain.quiz.entity.Quiz;
import com.sokpulee.crescendo.domain.quiz.entity.QuizAttempt;
import com.sokpulee.crescendo.domain.quiz.entity.QuizQuestion;
import com.sokpulee.crescendo.domain.quiz.entity.QuizQuestionAnswer;
import com.sokpulee.crescendo.domain.quiz.repository.QuizAttemptRepository;
import com.sokpulee.crescendo.domain.quiz.repository.quiz.QuizRepository;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.domain.user.repository.UserRepository;
import com.sokpulee.crescendo.global.exception.custom.QuizNotFoundException;
import com.sokpulee.crescendo.global.exception.custom.UserNotFoundException;
import com.sokpulee.crescendo.global.util.file.FileSaveHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final UserRepository userRepository;
    private final FileSaveHelper fileSaveHelper;

    @Override
    public void createQuiz(Long loggedInUserId, QuizCreateRequest quizCreateRequest) {

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        String saveQuizThumbnailImagePath = fileSaveHelper.saveQuizThumbnailImage(quizCreateRequest.getThumbnail());

        Quiz quiz = Quiz.builder()
                .user(user)
                .title(quizCreateRequest.getTitle())
                .content(quizCreateRequest.getContent())
                .questionNum(quizCreateRequest.getQuestionNum())
                .thumbnailPath(saveQuizThumbnailImagePath)
                .build();

        for(QuizQuestionRequest questionDTO : quizCreateRequest.getQuestionList()) {

            String saveQuizQuestionImagePath = fileSaveHelper.saveQuizQuestionImage(questionDTO.getQuizImage());

            QuizQuestion quizQuestion = QuizQuestion.builder()
                    .quizImagePath(saveQuizQuestionImagePath)
                    .build();

            for(String answer : questionDTO.getAnswer()) {
                QuizQuestionAnswer quizQuestionAnswer = QuizQuestionAnswer
                        .builder()
                        .answer(answer)
                        .build();

                quizQuestion.addQuestionAnswer(quizQuestionAnswer);
            }

            quiz.addQuizQuestion(quizQuestion);
        }

        quizRepository.save(quiz);
    }

    @Override
    public QuizStartResponse startQuiz(Long quizId) {
        Quiz quiz = quizRepository.findQuizWithDetailsById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid quiz ID: " + quizId));

        List<QuizQuestionDTO> questionList = quiz.getQuestions().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new QuizStartResponse(questionList);
    }

    private QuizQuestionDTO convertToDTO(QuizQuestion quizQuestion) {
        List<String> answers = quizQuestion.getQuestionAnswers().stream()
                .map(QuizQuestionAnswer::getAnswer)
                .toList();

        return new QuizQuestionDTO(quizQuestion.getQuizImagePath(), answers);
    }

    public QuizEndResponse endQuiz(Long quizId, QuizEndRequest quizEndRequest) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(QuizNotFoundException::new);

        quiz.hit();

        // 사용자의 정답 개수를 기록
        QuizAttempt quizAttempt = QuizAttempt.builder()
                .quiz(quiz)
                .score(quizEndRequest.getCorrectAnswerCount())
                .build();
        quizAttemptRepository.save(quizAttempt);

        List<QuizAttempt> attempts = quizAttemptRepository.findByQuizId(quizId);

        long betterThanCount = attempts.stream()
                .filter(attempt -> attempt.getScore() > quizEndRequest.getCorrectAnswerCount())
                .count();

        int rankPercentage = (int) (((double) betterThanCount / attempts.size()) * 100);

        QuizEndResponse response = new QuizEndResponse(100 - rankPercentage);

        return response;
    }

    public Page<QuizListResponse> getQuizzes(String title, Pageable pageable) {
        return quizRepository.findQuizzes(title, pageable);
    }
}
