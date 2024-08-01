package com.sokpulee.crescendo.domain.dm.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRequest {

    private Long dmGroupId;
    private Long userId;
    private String message;

    public ChatRequest(Long dmGroupId, Long userId, String message) {
        this.dmGroupId = dmGroupId;
        this.userId = userId;
        this.message = message;
    }
}
