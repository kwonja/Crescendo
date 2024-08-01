package com.sokpulee.crescendo.domain.dm.dto.response;

import com.sokpulee.crescendo.domain.dm.entity.DmGroup;
import lombok.Getter;

@Getter
public class DMGroupGetResponse {

    private Long dmGroupId;

    public DMGroupGetResponse(DmGroup dmGroup) {
        this.dmGroupId = dmGroup.getId();
    }
}
