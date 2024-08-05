package com.sokpulee.crescendo.domain.feed.entity;

import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.global.TimeStampedEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedComment extends TimeStampedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_feed_comment_id")
    private FeedComment parentFeedComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 200)
    private String content;

    private int likeCnt;

    private int replyCnt;

    @Builder
    public FeedComment(Feed feed, User user, String content, FeedComment parentFeedComment,int likeCnt, int replyCnt) {
        this.feed = feed;
        this.user = user;
        this.content = content;
        this.parentFeedComment = parentFeedComment;
        this.likeCnt = likeCnt;
        this.replyCnt = replyCnt;
    }

    public void changeComment(String content){
        this.content = content;
    }

    public void minusLikeCnt(){ likeCnt--; }

    public void plusLikeCnt(){
        likeCnt++;
    }

    public void plusReplyCnt(){ replyCnt++; }

    public void minusReplyCnt(){ replyCnt--; }

}
