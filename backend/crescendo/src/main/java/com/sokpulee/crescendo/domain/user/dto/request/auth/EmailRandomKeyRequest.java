package com.sokpulee.crescendo.domain.user.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EmailRandomKeyRequest {

    @NotBlank(message = "이메일은 필수값입니다.")
    private final String email;

    public EmailRandomKeyRequest(final String email) {
        this.email = email;
    }
}
