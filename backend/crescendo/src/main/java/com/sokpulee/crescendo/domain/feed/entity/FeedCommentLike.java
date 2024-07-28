package com.sokpulee.crescendo.domain.feed.entity;

import com.sokpulee.crescendo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedCommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedCommentLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_comment_id")
    private FeedComment feedComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}