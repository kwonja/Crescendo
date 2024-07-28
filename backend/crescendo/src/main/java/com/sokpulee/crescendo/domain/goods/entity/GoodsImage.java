package com.sokpulee.crescendo.domain.goods.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoodsImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goodsImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id")
    private Goods goods;

    @Column(length = 500)
    private String imagePath;
}