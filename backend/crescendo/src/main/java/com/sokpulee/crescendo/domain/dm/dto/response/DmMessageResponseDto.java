package com.sokpulee.crescendo.domain.dm.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DmMessageResponseDto {

    private Long dmMessageId;
    private String message;
    private LocalDateTime createdAt;
    private Long writerId;
    private String writerNickname;
    private String writerProfilePath;

    public DmMessageResponseDto(Long dmMessageId, String message, LocalDateTime createdAt, Long writerId, String writerNickname, String writerProfilePath) {
        this.dmMessageId = dmMessageId;
        this.message = message;
        this.createdAt = createdAt;
        this.writerId = writerId;
        this.writerNickname = writerNickname;
        this.writerProfilePath = writerProfilePath;
    }
}
