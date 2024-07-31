package com.sokpulee.crescendo.domain.user.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailRandomKeyRequest {

    @NotBlank(message = "이메일은 필수값입니다.")
    private String email;

    public EmailRandomKeyRequest(String email) {
        this.email = email;
    }
}
