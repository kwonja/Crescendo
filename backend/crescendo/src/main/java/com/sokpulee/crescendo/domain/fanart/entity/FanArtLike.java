package com.sokpulee.crescendo.domain.fanart.entity;

import com.sokpulee.crescendo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FanArtLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fanArtLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fan_art_id")
    private FanArt fanArt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public FanArtLike(FanArt fanArt, User user) {
        this.fanArt = fanArt;
        this.user = user;
    }
}
