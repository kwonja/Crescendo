package com.sokpulee.crescendo.domain.alarm.entity;

import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.global.CreatedAtEntity;
import com.sokpulee.crescendo.global.TimeStampedEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm extends CreatedAtEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alarm_channel_id")
    private AlarmChannel alarmChannel;

    private Long relatedId;

    @Column(length = 100)
    private String content;

    private boolean isRead;

    @Builder
    public Alarm(User user, AlarmChannel alarmChannel, Long relatedId, String content) {
        this.user = user;
        this.alarmChannel = alarmChannel;
        this.relatedId = relatedId;
        this.content = content;
    }

    public void readAlarm() {
        isRead = true;
    }
}