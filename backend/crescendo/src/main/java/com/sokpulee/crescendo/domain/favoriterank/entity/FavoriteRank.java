package com.sokpulee.crescendo.domain.favoriterank.entity;

import com.sokpulee.crescendo.domain.idol.entity.Idol;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.global.CreatedAtEntity;
import com.sokpulee.crescendo.global.TimeStampedEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteRank extends CreatedAtEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_rank_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idol_id")
    private Idol idol;

    @Column(length = 500)
    private String favoriteIdolImagePath;

    @Builder
    public FavoriteRank(User user, Idol idol, String favoriteIdolImagePath) {
        this.user = user;
        this.idol = idol;
        this.favoriteIdolImagePath = favoriteIdolImagePath;
    }
}