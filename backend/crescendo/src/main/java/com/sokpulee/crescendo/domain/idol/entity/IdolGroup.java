package com.sokpulee.crescendo.domain.idol.entity;

import com.sokpulee.crescendo.global.TimeStampedEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IdolGroup extends TimeStampedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idolGroupId;

    @Column(length = 50)
    private String name;

    private Integer peopleNum;

    @Column(length = 500)
    private String introduction;

    @Column(length = 500)
    private String profile;

    @Column(length = 500)
    private String banner;
}