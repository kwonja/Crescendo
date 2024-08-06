package com.sokpulee.crescendo.domain.alarm.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetAlarmResponse {
    private Long alarmId;
    private Long alarmChannelId;
    private Long relatedId;
    private String content;
    private Boolean isRead;
    private LocalDateTime createdAt;

    public GetAlarmResponse(Long alarmId, Long alarmChannelId, Long relatedId, String content, Boolean isRead, LocalDateTime createdAt) {
        this.alarmId = alarmId;
        this.alarmChannelId = alarmChannelId;
        this.relatedId = relatedId;
        this.content = content;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }
}
