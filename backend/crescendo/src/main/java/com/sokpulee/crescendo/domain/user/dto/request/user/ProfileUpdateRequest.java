package com.sokpulee.crescendo.domain.user.dto.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class ProfileUpdateRequest {

    @NotNull(message = "프로필 이미지는 필수입니다.")
    private MultipartFile profileImage;

    public ProfileUpdateRequest(MultipartFile profileImage) {
        this.profileImage = profileImage;
    }
}
