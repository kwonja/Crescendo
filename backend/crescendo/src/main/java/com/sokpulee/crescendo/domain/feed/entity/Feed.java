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

    @Column(length = 50)
    private String title;

    @Column(length = 500)
    private String content;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedHashtag> hashtagList = new ArrayList<>();

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedImage> imageList = new ArrayList<>();

    @Builder
    public Feed(IdolGroup idolGroup, User user, String title, String content) {
        this.idolGroup = idolGroup;
        this.user = user;
        this.title = title;
        this.content = content;
    }

    public void addHashtag(FeedHashtag hashtag) {
        hashtagList.add(hashtag);
        hashtag.changeFeed(this);
    }

    public void addImage(FeedImage image) {
        imageList.add(image);
        image.changeFeed(this);
    }

    public void changeFeed(IdolGroup idolGroup, String title, String content) {
        this.idolGroup = idolGroup;
        this.title = title;
        this.content = content;
    }


}
