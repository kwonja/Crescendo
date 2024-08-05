package com.sokpulee.crescendo.domain.idol.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IdealWorldCupFinishRequest {

    @NotNull(message = "아이돌ID는 필수 값 입니다.")
    private Long idolId;

    public IdealWorldCupFinishRequest(Long idolId) {
        this.idolId = idolId;
    }
}
