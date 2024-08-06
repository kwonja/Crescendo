package com.sokpulee.crescendo.domain.user.entity;

import com.sokpulee.crescendo.global.CreatedAtEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailAuth extends CreatedAtEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emailAuthId;

    private String randomKey;

    private LocalDateTime expiresAt;

    @Builder
    public EmailAuth(String randomKey, LocalDateTime expiresAt) {
        this.randomKey = randomKey;
        this.expiresAt = expiresAt;
    }
}