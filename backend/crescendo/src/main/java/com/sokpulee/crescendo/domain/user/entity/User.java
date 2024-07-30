package com.sokpulee.crescendo.domain.user.entity;

import com.sokpulee.crescendo.domain.idol.entity.Idol;
import com.sokpulee.crescendo.global.util.encrypt.EnctyptHelper;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

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

    @Builder
    public User(Idol idol, String email, String password, String nickname, String profilePath, String introduction, String refreshToken) {
        this.idol = idol;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profilePath = profilePath;
        this.introduction = introduction;
        this.refreshToken = refreshToken;
    }

    public void encryptPassword(EnctyptHelper enctyptHelper) {
        this.password = enctyptHelper.encrypt(this.password);
    }

    public void changeRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void changeProfilePath(String savePath) {
        this.profilePath = savePath;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateNickname(String newNickname) {
        this.nickname = newNickname;
    }
}
