package com.sokpulee.crescendo.domain.user.dto.request.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateFavoriteIdolRequest {

    Long idolId;

    public UpdateFavoriteIdolRequest(Long idolId) {
        this.idolId = idolId;
    }
}
