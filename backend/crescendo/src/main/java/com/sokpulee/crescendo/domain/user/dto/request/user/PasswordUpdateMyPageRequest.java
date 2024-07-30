package com.sokpulee.crescendo.domain.user.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PasswordUpdateMyPageRequest {

    @NotBlank(message = "현재 비밀번호 값은 필수입니다.")
    private final String currentPassword;

    @NotBlank(message = "새 비밀번호 값은 필수입니다.")
    private final String newPassword;

    public PasswordUpdateMyPageRequest(final String currentPassword, final String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
}
