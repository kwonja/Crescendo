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
public class GoodsLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goodsLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id")
    private Goods goods;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public GoodsLike(Goods goods, User user) {
        this.goods = goods;
        this.user = user;
    }
}
