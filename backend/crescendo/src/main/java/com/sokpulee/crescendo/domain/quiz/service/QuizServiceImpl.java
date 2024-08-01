package com.sokpulee.crescendo.domain.quiz.service;

import com.sokpulee.crescendo.domain.quiz.dto.request.QuizCreateRequest;
import com.sokpulee.crescendo.domain.quiz.entity.Quiz;
import com.sokpulee.crescendo.domain.quiz.entity.QuizQuestion;
import com.sokpulee.crescendo.domain.quiz.entity.QuizQuestionAnswer;
import com.sokpulee.crescendo.domain.quiz.repository.QuizQuestionAnswerRepository;
import com.sokpulee.crescendo.domain.quiz.repository.QuizQuestionRepository;
import com.sokpulee.crescendo.domain.quiz.repository.QuizRepository;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.domain.user.repository.UserRepository;
import com.sokpulee.crescendo.global.exception.custom.UserNotFoundException;
import com.sokpulee.crescendo.global.util.file.FileSaveHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizQuestionAnswerRepository quizQuestionAnswerRepository;
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

        for(QuizCreateRequest.QuizQuestionDTO questionDTO : quizCreateRequest.getQuestionList()) {

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
}
