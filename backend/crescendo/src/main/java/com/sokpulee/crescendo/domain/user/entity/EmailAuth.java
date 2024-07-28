package com.sokpulee.crescendo.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emailAuthId;

    private Integer randomKey;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}