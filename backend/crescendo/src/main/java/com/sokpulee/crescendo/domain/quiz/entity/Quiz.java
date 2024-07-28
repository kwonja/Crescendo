package com.sokpulee.crescendo.domain.quiz.entity;

import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.global.TimeStampedEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quiz extends TimeStampedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizId;

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
}