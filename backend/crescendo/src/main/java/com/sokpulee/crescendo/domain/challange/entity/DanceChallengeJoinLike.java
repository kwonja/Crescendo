package com.sokpulee.crescendo.domain.challange.entity;

import com.sokpulee.crescendo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DanceChallengeJoinLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long danceChallengeJoinLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dance_challenge_join_id")
    private DanceChallengeJoin danceChallengeJoin;
}