package com.sokpulee.crescendo.domain.favoriterank.entity;

import com.sokpulee.crescendo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteRankVoting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteRankVotingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}