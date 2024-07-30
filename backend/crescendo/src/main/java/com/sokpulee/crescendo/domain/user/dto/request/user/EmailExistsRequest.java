package com.sokpulee.crescendo.domain.user.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EmailExistsRequest {

    @NotBlank(message = "이메일은 필수값입니다.")
    private final String email;

    public EmailExistsRequest(final String email) {
        this.email = email;
    }
}
