package com.sokpulee.crescendo.domain.fanart.entity;

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
public class FanArt extends TimeStampedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fanArtId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idol_group_id")
    private IdolGroup idolGroup;

    @Column(length = 50)
    private String title;

    @Column(length = 500)
    private String content;

    @OneToMany(mappedBy = "fanArt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FanArtImage> imageList = new ArrayList<>();

    @Builder
    public FanArt(User user, IdolGroup idolGroup, String title, String content) {
        this.user = user;
        this.idolGroup = idolGroup;
        this.title = title;
        this.content = content;
    }

    public void addImage(FanArtImage image) {
        imageList.add(image);
        image.changeFanArt(this);
    }


    public void changeFanArt(IdolGroup idolGroup, String title, String content) {
        this.idolGroup = idolGroup;
        this.title = title;
        this.content = content;
    }
}