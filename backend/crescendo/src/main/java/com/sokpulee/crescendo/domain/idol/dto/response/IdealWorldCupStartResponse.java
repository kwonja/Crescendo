package com.sokpulee.crescendo.domain.idol.dto.response;

import com.sokpulee.crescendo.domain.idol.dto.IdolInfoDto;
import lombok.Getter;

import java.util.List;

@Getter
public class IdealWorldCupStartResponse {
    private List<IdolInfoDto> idolList;

    public IdealWorldCupStartResponse(List<IdolInfoDto> idolList) {
        this.idolList = idolList;
    }
}
