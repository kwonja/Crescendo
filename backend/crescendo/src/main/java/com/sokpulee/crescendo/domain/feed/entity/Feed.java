package com.sokpulee.crescendo.domain.feed.entity;

import com.sokpulee.crescendo.domain.idol.entity.IdolGroup;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.global.TimeStampedEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed extends TimeStampedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idol_group_id")
    private IdolGroup idolGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 500)
    private String content;

    private Integer likeCnt;

    private Integer commentCnt;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedHashtag> hashtagList = new ArrayList<>();

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedImage> imageList = new ArrayList<>();

    @Builder
    public Feed(IdolGroup idolGroup, User user, String content,Integer likeCnt, Integer commentCnt) {
        this.idolGroup = idolGroup;
        this.user = user;
        this.content = content;
        this.likeCnt = likeCnt;
        this.commentCnt = commentCnt;
    }

    public void addHashtag(FeedHashtag hashtag) {
        hashtagList.add(hashtag);
        hashtag.changeFeed(this);
    }

    public void addImage(FeedImage image) {
        imageList.add(image);
        image.changeFeed(this);
    }

    public List<String> getImagePathList(List<FeedImage> imageList){
        List<String> list = new ArrayList<>();

        for (FeedImage feedImage : imageList) {
            list.add(feedImage.getImagePath());
        }
        return list;
    }

    public List<String> getTagList(List<FeedHashtag> hashtagList){
        List<String> list = new ArrayList<>();

        for (FeedHashtag hashtag : hashtagList) {
            list.add(hashtag.getTag());
        }
        return list;
    }

    public void changeFeed(String content) {
        this.content = content;
    }

    public void minusLikeCnt() {
        likeCnt--;
    }

    public void plusLikeCnt(){
        likeCnt++;
    }

    public void plusCommentCnt(){commentCnt++; }

    public void minusCommentCnt(int replyCnt){commentCnt-= replyCnt + 1; }


}
