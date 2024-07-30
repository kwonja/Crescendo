package com.sokpulee.crescendo.domain.user.dto.request.auth;

import com.sokpulee.crescendo.domain.idol.entity.Idol;
import com.sokpulee.crescendo.domain.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignUpRequest {

    @NotBlank(message = "이메일은 필수값 입니다.")
    @Email(message = "유효하지 않은 이메일입니다.")
    private final String email;

    @NotBlank(message = "패스워드는 필수값 입니다.")
    @Size(min = 8, max = 14, message = "패스워드는 8 ~ 14 자리 이어야 합니다.")
    private final String password;

    @NotBlank(message = "닉네임은 필수값 입니다.")
    @Size(min = 3, max = 20, message = "닉네임은 3 ~ 20 자리 이어야 합니다.")
    private final String nickname;

    @NotNull(message = "Idol ID는 필수값 입니다.")
    private final Long idolId;

    @NotNull(message = "이메일 인증 아이디는 필수값 입니다.")
    private final Long emailAuthId;

    @NotBlank(message = "랜덤 키 값은 필수값 입니다.")
    private final String randomKey;

    public SignUpRequest(final String email, final String password, final String nickname, final Long idolId, final Long emailAuthId, final String randomKey) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.idolId = idolId;
        this.emailAuthId = emailAuthId;
        this.randomKey = randomKey;
    }

    public User toEntity(Idol idol) {
        return User.builder()
                .idol(idol)
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();
    }

}
