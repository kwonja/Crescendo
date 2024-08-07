package com.sokpulee.crescendo.domain.goods.entity;

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
public class GoodsComment extends TimeStampedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goodsCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id")
    private Goods goods;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_goods_comment_id")
    private GoodsComment parentGoodsComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 200)
    private String content;

    private int likeCnt;

    private int replyCnt;

    @Builder
    public GoodsComment(Goods goods, User user, String content, GoodsComment parentGoodsComment, int likeCnt, int replyCnt) {
        this.goods = goods;
        this.user = user;
        this.content = content;
        this.parentGoodsComment = parentGoodsComment;
        this.likeCnt = likeCnt;
        this.replyCnt = replyCnt;
    }

    public void changeComment(String content){
        this.content = content;
    }

    public void minusLikeCnt(){ likeCnt--; }

    public void plusLikeCnt(){ likeCnt++; }

    public void plusReplyCnt(){ replyCnt++; }

    public void minusReplyCnt(){ replyCnt--; }
}