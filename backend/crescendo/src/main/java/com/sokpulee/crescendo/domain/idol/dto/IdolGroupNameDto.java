package com.sokpulee.crescendo.domain.idol.dto;

import lombok.Getter;

@Getter
public class IdolGroupNameDto {

    private Long IdolGroupId;
    private String IdolGroupName;

    public IdolGroupNameDto(Long idolGroupId, String idolGroupName) {
        IdolGroupId = idolGroupId;
        IdolGroupName = idolGroupName;
    }
}
