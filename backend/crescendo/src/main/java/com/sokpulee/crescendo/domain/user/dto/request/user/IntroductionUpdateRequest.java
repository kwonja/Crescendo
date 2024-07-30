package com.sokpulee.crescendo.domain.user.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IntroductionUpdateRequest {

    @NotBlank(message = "자기소개는 필수값입니다.")
    private String introduction;

    public IntroductionUpdateRequest(String introduction) {
        this.introduction = introduction;
    }
}
