package com.sokpulee.crescendo.domain.dm.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageRequest {

    private Long dmGroupId;
    private Long writerId;
    private Long recipientId;
    private String message;

    public MessageRequest(Long dmGroupId, Long writerId, Long recipientId, String message) {
        this.dmGroupId = dmGroupId;
        this.writerId = writerId;
        this.recipientId = recipientId;
        this.message = message;
    }
}
