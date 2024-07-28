package com.sokpulee.crescendo.domain.alarm.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmChannel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alarmChannelId;

    @Column(length = 50)
    private String name;
}