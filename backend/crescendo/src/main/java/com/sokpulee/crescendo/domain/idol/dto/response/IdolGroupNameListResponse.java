package com.sokpulee.crescendo.domain.idol.dto.response;

import com.sokpulee.crescendo.domain.idol.dto.IdolGroupNameDto;
import lombok.Getter;

import java.util.List;

@Getter
public class IdolGroupNameListResponse {

    private List<IdolGroupNameDto> idolGroupList;

    public IdolGroupNameListResponse(List<IdolGroupNameDto> idolGroupList) {
        this.idolGroupList = idolGroupList;
    }
}
