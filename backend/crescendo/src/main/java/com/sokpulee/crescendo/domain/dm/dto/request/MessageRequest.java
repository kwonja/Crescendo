package com.sokpulee.crescendo.domain.dm.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageRequest {

    private Long dmGroupId;
    private Long writerId;
    private String message;

    public MessageRequest(Long dmGroupId, Long writerId, String message) {
        this.dmGroupId = dmGroupId;
        this.writerId = writerId;
        this.message = message;
    }
}
