package com.sokpulee.crescendo.domain.goods.entity;

import com.sokpulee.crescendo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoodsCommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goodsCommentLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_comment_id")
    private GoodsComment goodsComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public GoodsCommentLike(GoodsComment goodsComment, User user) {
        this.goodsComment = goodsComment;
        this.user = user;
    }
}