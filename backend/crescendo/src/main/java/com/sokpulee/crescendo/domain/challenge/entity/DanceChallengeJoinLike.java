package com.sokpulee.crescendo.domain.challenge.entity;

import com.sokpulee.crescendo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DanceChallengeJoinLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dance_challenge_join_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dance_challenge_join_id")
    private DanceChallengeJoin danceChallengeJoin;

    @Builder
    public DanceChallengeJoinLike(User user, DanceChallengeJoin danceChallengeJoin) {
        this.user = user;
        this.danceChallengeJoin = danceChallengeJoin;
    }
}