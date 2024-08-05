package com.sokpulee.crescendo.domain.dm.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MyDmGroupResponseDto {

    private Long dmGroupId;
    private Long opponentId;
    private String opponentProfilePath;
    private String opponentNickName;
    private String lastChatting;
    private String lastChattingTime;

    public MyDmGroupResponseDto(Long dmGroupId, Long opponentId, String opponentProfilePath, String opponentNickName, String lastChatting, String lastChattingTime) {
        this.dmGroupId = dmGroupId;
        this.opponentId = opponentId;
        this.opponentProfilePath = opponentProfilePath;
        this.opponentNickName = opponentNickName;
        this.lastChatting = lastChatting;
        this.lastChattingTime = lastChattingTime;
    }
}
