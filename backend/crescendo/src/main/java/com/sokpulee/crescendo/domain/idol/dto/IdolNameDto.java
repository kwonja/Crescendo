package com.sokpulee.crescendo.domain.idol.dto;

import lombok.Getter;

@Getter
public class IdolNameDto {

    private Long idolId;
    private String idolName;

    public IdolNameDto(Long idolId, String idolName) {
        this.idolId = idolId;
        this.idolName = idolName;
    }
}
