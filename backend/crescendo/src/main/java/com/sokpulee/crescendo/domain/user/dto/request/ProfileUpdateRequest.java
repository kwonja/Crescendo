package com.sokpulee.crescendo.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class ProfileUpdateRequest {

    @NotNull(message = "프로필 이미지는 필수입니다.")
    private MultipartFile profileImage;

}
