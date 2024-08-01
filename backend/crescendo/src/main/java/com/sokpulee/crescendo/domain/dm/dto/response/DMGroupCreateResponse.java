package com.sokpulee.crescendo.domain.dm.dto.response;

import com.sokpulee.crescendo.domain.dm.entity.DmGroup;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DMGroupCreateResponse {

    private Long dmGroupId;

    public DMGroupCreateResponse(DmGroup dmGroup) {
        this.dmGroupId = dmGroup.getId();
    }
}
