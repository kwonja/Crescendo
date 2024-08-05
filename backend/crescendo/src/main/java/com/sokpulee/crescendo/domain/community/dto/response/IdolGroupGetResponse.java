package com.sokpulee.crescendo.domain.community.dto.response;

import lombok.Getter;

@Getter
public class IdolGroupGetResponse {
    private Long idolGroupId;
    private String name;
    private String profile;

    public IdolGroupGetResponse(Long idolGroupId, String name, String profile) {
        this.idolGroupId = idolGroupId;
        this.name = name;
        this.profile = profile;
    }
}
