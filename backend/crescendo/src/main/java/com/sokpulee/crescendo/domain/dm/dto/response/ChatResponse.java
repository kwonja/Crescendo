package com.sokpulee.crescendo.domain.dm.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatResponse {

    private Long dmMessageId;
    private String message;
    private LocalDateTime createdAt;
    private Long writerId;
    private String writerNickName;
    private String writerProfilePath;

    public ChatResponse(Long dmMessageId, String message, LocalDateTime createdAt, Long writerId, String writerNickName, String writerProfilePath) {
        this.dmMessageId = dmMessageId;
        this.message = message;
        this.createdAt = createdAt;
        this.writerId = writerId;
        this.writerNickName = writerNickName;
        this.writerProfilePath = writerProfilePath;
    }
}
