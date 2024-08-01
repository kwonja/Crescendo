package com.sokpulee.crescendo.domain.quiz.entity;

import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.global.CreatedAtEntity;
import com.sokpulee.crescendo.global.TimeStampedEntity;
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
public class Quiz extends CreatedAtEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 50)
    private String title;

    @Column(length = 200)
    private String content;

    private Integer questionNum;

    @Column(length = 500)
    private String thumbnailPath;

    private Integer hit;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    List<QuizQuestion> questions = new ArrayList<>();

    public void addQuizQuestion(QuizQuestion quizQuestion) {
        this.questions.add(quizQuestion);
        quizQuestion.changeQuiz(this);
    }

    @Builder
    public Quiz(User user, String title, String content, Integer questionNum, String thumbnailPath, Integer hit, List<QuizQuestion> questions) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.questionNum = questionNum;
        this.thumbnailPath = thumbnailPath;
        this.hit = hit;
        this.questions = questions;
    }
}