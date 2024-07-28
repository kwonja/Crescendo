package com.sokpulee.crescendo.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAlarmSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNotificationSettingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Boolean isEnabled;
}
