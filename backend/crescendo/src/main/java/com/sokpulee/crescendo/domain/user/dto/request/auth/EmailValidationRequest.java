package com.sokpulee.crescendo.domain.user.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class EmailValidationRequest {

    @NotNull(message = "emailAuthId는 필수 값 입니다.")
    private final Long emailAuthId;

    @NotBlank(message = "randomKey는 필수 값 입니다.")
    private final String randomKey;

    public EmailValidationRequest(final Long emailAuthId, final String randomKey) {
        this.emailAuthId = emailAuthId;
        this.randomKey = randomKey;
    }
}
