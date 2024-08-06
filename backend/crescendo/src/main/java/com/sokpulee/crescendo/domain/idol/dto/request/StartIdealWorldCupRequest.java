package com.sokpulee.crescendo.domain.idol.dto.request;

import com.sokpulee.crescendo.domain.idol.enums.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
public class StartIdealWorldCupRequest {

    @NotNull(message = "아이돌 명수는 필수 값입니다.")
    Integer num;

    @NotNull(message = "아이돌 성별은 필수 값입니다.")
    Gender gender;

    public StartIdealWorldCupRequest(Integer num, Gender gender) {
        this.num = num;
        this.gender = gender;
    }
}
