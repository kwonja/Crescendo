package com.sokpulee.crescendo.domain.idol.entity;

import com.sokpulee.crescendo.domain.idol.enums.Gender;
import com.sokpulee.crescendo.global.TimeStampedEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Idol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idol_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idol_group_id")
    private IdolGroup idolGroup;

    @Column(length = 50)
    private String name;

    @Column(length = 500)
    private String introduction;

    @Column(length = 50)
    private String part;

    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private Gender gender;

    @Column(length = 500)
    private String profile;

    @Column(length = 500)
    private String profile2;

    private Integer winNum;

    public void updateProfile2(String favoriteImagePath) {
        this.profile2 = favoriteImagePath;
    }

    public void plusWinNum() {
        this.winNum++;
    }
}