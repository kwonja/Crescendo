package com.sokpulee.crescendo.domain.idol.dto.response;

import com.sokpulee.crescendo.domain.idol.dto.IdolNameDto;
import lombok.Getter;

import java.util.List;

@Getter
public class IdolNameListResponse {
    private List<IdolNameDto> idolList;

    public IdolNameListResponse(List<IdolNameDto> idolList) {
        this.idolList = idolList;
    }
}
