package com.sokpulee.crescendo.domain.goods.entity;

import com.sokpulee.crescendo.domain.fanart.entity.FanArtImage;
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
public class Goods extends TimeStampedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goodsId;

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

    private Integer likeCnt;

    private Integer commentCnt;

    @OneToMany(mappedBy = "goods",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<GoodsImage> imageList = new ArrayList<>();

    @Builder
    public Goods(IdolGroup idolGroup, User user, String title, String content, Integer likeCnt, Integer commentCnt) {
        this.idolGroup = idolGroup;
        this.user = user;
        this.title = title;
        this.content = content;
        this.likeCnt = likeCnt;
        this.commentCnt = commentCnt;
    }

    public void addImage(GoodsImage goodsImage){
        imageList.add(goodsImage);
        goodsImage.changeGoods(this);
    }

    public List<String> getImagePathList(List<GoodsImage> imageList){
        List<String> list = new ArrayList<>();

        for (GoodsImage goodsImage : imageList) {
            list.add(goodsImage.getImagePath());
        }
        return list;
    }

    public void changeGoods(String title, String content) {
        this.title = title;
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
