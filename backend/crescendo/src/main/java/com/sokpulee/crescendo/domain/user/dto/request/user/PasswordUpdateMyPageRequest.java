package com.sokpulee.crescendo.domain.user.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PasswordUpdateMyPageRequest {

    @NotBlank(message = "현재 비밀번호 값은 필수입니다.")
    private String currentPassword;

    @NotBlank(message = "새 비밀번호 값은 필수입니다.")
    private String newPassword;

    public PasswordUpdateMyPageRequest(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
}
