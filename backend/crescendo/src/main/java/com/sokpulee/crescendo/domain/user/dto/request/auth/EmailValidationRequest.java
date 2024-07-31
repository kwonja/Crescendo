package com.sokpulee.crescendo.domain.user.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailValidationRequest {

    @NotNull(message = "emailAuthId는 필수 값 입니다.")
    private Long emailAuthId;

    @NotBlank(message = "randomKey는 필수 값 입니다.")
    private String randomKey;

    public EmailValidationRequest(Long emailAuthId, String randomKey) {
        this.emailAuthId = emailAuthId;
        this.randomKey = randomKey;
    }
}
