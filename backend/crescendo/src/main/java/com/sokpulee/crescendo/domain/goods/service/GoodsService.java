package com.sokpulee.crescendo.domain.goods.service;

import com.sokpulee.crescendo.domain.goods.dto.request.GoodsAddRequest;
import com.sokpulee.crescendo.domain.goods.dto.request.GoodsCommentAddRequest;
import com.sokpulee.crescendo.domain.goods.entity.Goods;

public interface GoodsService {
    void addGoods(Long loggedInUserId, GoodsAddRequest goodsAddRequest);

    void addGoodsComment(Long loggedInUserId, Long goodsId, GoodsCommentAddRequest goodsCommentAddRequest);

    void addGoodsReply(Long loggedInUserId,Long goodsId, Long goodsCommentId, GoodsCommentAddRequest goodsReplyAddRequest);

    void deleteGoods(Long loggedInUserId,Long goodsId);
}
