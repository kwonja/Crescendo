package com.sokpulee.crescendo.domain.user.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class IntroductionUpdateRequest {

    @NotBlank(message = "자기소개는 필수값입니다.")
    private final String introduction;

    public IntroductionUpdateRequest(final String introduction) {
        this.introduction = introduction;
    }
}
