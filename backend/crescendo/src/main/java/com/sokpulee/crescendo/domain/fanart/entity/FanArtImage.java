package com.sokpulee.crescendo.domain.fanart.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FanArtImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fanArtImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fan_art_id")
    private FanArt fanArt;

    @Column(length = 500)
    private String imagePath;
}