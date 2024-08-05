package com.sokpulee.crescendo.domain.challenge.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class JoinDanceChallengeRequest {

    @NotNull(message = "비디오 파일은 필수 값 입니다.")
    private MultipartFile video;
}
