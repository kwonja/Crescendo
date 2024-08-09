package com.sokpulee.crescendo.domain.dm.dto.response;

import com.sokpulee.crescendo.domain.dm.entity.DmMessage;
import com.sokpulee.crescendo.domain.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageResponse {

    private Long dmGroupId;
    private Long dmMessageId;
    private String message;
    private LocalDateTime createdAt;
    private Long recipientId;
    private Long writerId;
    private String writerNickName;
    private String writerProfilePath;

    public MessageResponse(DmMessage dmMessage, User recipient, User writer) {
        this.dmGroupId = dmMessage.getDmGroup().getId();
        this.dmMessageId = dmMessage.getId();
        this.message = dmMessage.getContent();
        this.createdAt = dmMessage.getCreatedAt();
        this.recipientId = recipient.getId();
        this.writerId = writer.getId();
        this.writerNickName = writer.getNickname();
        this.writerProfilePath = writer.getProfilePath();
    }
}
