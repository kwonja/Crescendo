package com.sokpulee.crescendo.domain.feed.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @Column(length = 500)
    private String imagePath;
}