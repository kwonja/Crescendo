package com.sokpulee.crescendo.domain.user.entity;

import com.sokpulee.crescendo.domain.idol.entity.Idol;
import com.sokpulee.crescendo.global.TimeStampedEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends TimeStampedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idol_id")
    private Idol idol;

    @Column(length = 50)
    private String email;

    @Column(length = 500)
    private String password;

    @Column(length = 50)
    private String nickname;

    @Column(length = 200)
    private String profilePath;

    @Column(length = 500)
    private String introduction;

    @Column(length = 1000)
    private String refreshToken;
}
