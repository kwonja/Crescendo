package com.sokpulee.crescendo.domain.quiz.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_question_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @Column(length = 500)
    private String quizImagePath;

    @OneToMany(mappedBy = "quizQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    List<QuizQuestionAnswer> questionAnswers = new ArrayList<>();

    @Builder
    public QuizQuestion(Quiz quiz, String quizImagePath, List<QuizQuestionAnswer> questionAnswers) {
        this.quiz = quiz;
        this.quizImagePath = quizImagePath;
        this.questionAnswers = questionAnswers;
    }

    public void addQuestionAnswer(QuizQuestionAnswer questionAnswer) {
        this.questionAnswers.add(questionAnswer);
        questionAnswer.changeQuizQuestion(this);
    }

    public void changeQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
}