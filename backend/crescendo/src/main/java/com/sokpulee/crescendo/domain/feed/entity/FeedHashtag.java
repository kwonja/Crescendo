package com.sokpulee.crescendo.domain.feed.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedHashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedHashtagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @Column(length = 50)
    private String tag;
}
