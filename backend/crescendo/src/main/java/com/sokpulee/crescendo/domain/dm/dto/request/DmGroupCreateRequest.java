package com.sokpulee.crescendo.domain.dm.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DmGroupCreateRequest {

    private Long opponentId;

    public DmGroupCreateRequest(Long opponentId) {
        this.opponentId = opponentId;
    }
}
