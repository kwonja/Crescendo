package com.sokpulee.crescendo.domain.alarm.entity;

import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.global.TimeStampedEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm extends TimeStampedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alarmId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alarm_channel_id")
    private AlarmChannel alarmChannel;

    private Long relatedId;

    @Column(length = 100)
    private String content;

    private Boolean isRead;
}