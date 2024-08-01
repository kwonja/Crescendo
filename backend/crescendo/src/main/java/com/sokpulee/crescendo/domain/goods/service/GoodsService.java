package com.sokpulee.crescendo.domain.goods.service;

import com.sokpulee.crescendo.domain.goods.dto.request.GoodsAddRequest;
import com.sokpulee.crescendo.domain.goods.dto.request.GoodsCommentAddRequest;
import com.sokpulee.crescendo.domain.goods.dto.request.GoodsCommentUpdateRequest;
import com.sokpulee.crescendo.domain.goods.dto.request.GoodsUpdateRequest;

public interface GoodsService {
    void addGoods(Long loggedInUserId, GoodsAddRequest goodsAddRequest);

    void addGoodsComment(Long loggedInUserId, Long goodsId, GoodsCommentAddRequest goodsCommentAddRequest);

    void addGoodsReply(Long loggedInUserId,Long goodsId, Long goodsCommentId, GoodsCommentAddRequest goodsReplyAddRequest);

    void deleteGoods(Long goodsId, Long loggedInUserId);

    void updateGoods(Long loggedInUserId,Long goodsId,GoodsUpdateRequest goodsUpdateRequest);

    void deleteGoodsComment(Long loggedInUserId,Long goodsId,Long goodsCommentId);

    void updateGoodsComment(Long loggedInUserId, Long goodsId,Long goodsCommentId, GoodsCommentUpdateRequest goodsCommentUpdateRequest);
}
