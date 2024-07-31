package com.sokpulee.crescendo.domain.favoriterank.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class FavoriteRankAddRequest {

    @NotNull(message = "최애 아이돌 ID")
    private Long idolId;

    @NotNull(message = "최애 아이돌 사진은 필수값입니다.")
    private MultipartFile favoriteIdolImage;

}
