package com.sokpulee.crescendo.domain.idol.dto;

import lombok.Getter;

@Getter
public class IdolInfoDto {

    private Long idolId;
    private String idolGroupName;
    private String idolName;
    private String idolProfilePath;

    public IdolInfoDto(Long idolId, String idolGroupName, String idolName, String idolProfilePath) {
        this.idolId = idolId;
        this.idolGroupName = idolGroupName;
        this.idolName = idolName;
        this.idolProfilePath = idolProfilePath;
    }
}
