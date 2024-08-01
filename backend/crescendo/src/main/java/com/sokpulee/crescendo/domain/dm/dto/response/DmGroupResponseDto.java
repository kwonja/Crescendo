package com.sokpulee.crescendo.domain.dm.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class DmGroupResponseDto {

    private Long dmGroupId;
    private Long opponentId;
    private String opponentProfilePath;
    private String opponentNickName;
    private String lastChatting;
    private LocalDateTime lastChattingTime;

    public DmGroupResponseDto(Long dmGroupId, Long opponentId, String opponentProfilePath, String opponentNickName, String lastChatting, LocalDateTime lastChattingTime) {
        this.dmGroupId = dmGroupId;
        this.opponentId = opponentId;
        this.opponentProfilePath = opponentProfilePath;
        this.opponentNickName = opponentNickName;
        this.lastChatting = lastChatting;
        this.lastChattingTime = lastChattingTime;
    }
}
