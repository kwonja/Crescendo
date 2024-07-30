package com.sokpulee.crescendo.domain.user.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdatePasswordRequest {

    @NotBlank(message = "이메일은 필수값입니다.")
    private final String email;

    @NotBlank(message = "새 비밀번호는 필수값입니다.")
    private final String newPassword;

    @NotNull(message = "이메일 인증 ID는 필수값입니다.")
    private final Long emailAuthId;

    @NotBlank(message = "이메일 인증 키는 필수값입니다.")
    private final String randomKey;

    public UpdatePasswordRequest(final String email, final String newPassword, final Long emailAuthId, final String randomKey) {
        this.email = email;
        this.newPassword = newPassword;
        this.emailAuthId = emailAuthId;
        this.randomKey = randomKey;
    }
}
