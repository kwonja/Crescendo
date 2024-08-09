package com.sokpulee.crescendo.domain.alarm.dto;

import com.sokpulee.crescendo.domain.alarm.entity.Alarm;
import com.sokpulee.crescendo.domain.alarm.entity.AlarmChannel;
import com.sokpulee.crescendo.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AlarmDto {

    private Long userId;
    private Long alarmChannelId;
    private Long relatedId;
    private String content;

    public AlarmDto(Long userId, Long alarmChannelId, Long relatedId, String content) {
        this.userId = userId;
        this.alarmChannelId = alarmChannelId;
        this.relatedId = relatedId;
        this.content = content;
    }

    public Alarm toEntity(User user, AlarmChannel alarmChannel) {

        return Alarm.builder()
                .user(user)
                .alarmChannel(alarmChannel)
                .relatedId(relatedId)
                .content(content)
                .build();
    }
}
