package com.sokpulee.crescendo.domain.dm.dto.response;

import com.sokpulee.crescendo.domain.dm.entity.DmMessage;
import com.sokpulee.crescendo.domain.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageResponse {

    private Long dmMessageId;
    private String message;
    private LocalDateTime createdAt;
    private Long writerId;
    private String writerNickName;
    private String writerProfilePath;

    public MessageResponse(DmMessage dmMessage, User user) {
        this.dmMessageId = dmMessage.getId();
        this.message = dmMessage.getContent();
        this.createdAt = dmMessage.getCreatedAt();
        this.writerId = user.getId();
        this.writerNickName = user.getNickname();
        this.writerProfilePath = user.getProfilePath();
    }
}
