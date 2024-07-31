package com.sokpulee.crescendo.domain.user.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdatePasswordRequest {

    @NotBlank(message = "이메일은 필수값입니다.")
    private String email;

    @NotBlank(message = "새 비밀번호는 필수값입니다.")
    private String newPassword;

    @NotNull(message = "이메일 인증 ID는 필수값입니다.")
    private Long emailAuthId;

    @NotBlank(message = "이메일 인증 키는 필수값입니다.")
    private String randomKey;

    public UpdatePasswordRequest(String email, String newPassword, Long emailAuthId, String randomKey) {
        this.email = email;
        this.newPassword = newPassword;
        this.emailAuthId = emailAuthId;
        this.randomKey = randomKey;
    }
}
