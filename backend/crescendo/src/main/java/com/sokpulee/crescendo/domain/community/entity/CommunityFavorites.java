package com.sokpulee.crescendo.domain.community.entity;

import com.sokpulee.crescendo.domain.idol.entity.IdolGroup;
import com.sokpulee.crescendo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityFavorites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long communityFavoritesId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idol_group_id")
    private IdolGroup idolGroup;

    @Builder
    public CommunityFavorites(User user, IdolGroup idolGroup) {
        this.user = user;
        this.idolGroup = idolGroup;
    }
}