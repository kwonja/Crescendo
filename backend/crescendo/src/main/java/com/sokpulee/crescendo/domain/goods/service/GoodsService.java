package com.sokpulee.crescendo.domain.goods.service;

import com.sokpulee.crescendo.domain.goods.dto.request.GoodsAddRequest;
import com.sokpulee.crescendo.domain.goods.dto.request.GoodsCommentAddRequest;
import com.sokpulee.crescendo.domain.goods.dto.request.GoodsCommentUpdateRequest;
import com.sokpulee.crescendo.domain.goods.dto.request.GoodsUpdateRequest;
import com.sokpulee.crescendo.domain.goods.dto.response.FavoriteGoodsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GoodsService {
    void addGoods(Long loggedInUserId, GoodsAddRequest goodsAddRequest);

    void addGoodsComment(Long loggedInUserId, Long goodsId, GoodsCommentAddRequest goodsCommentAddRequest);

    void addGoodsReply(Long loggedInUserId,Long goodsId, Long goodsCommentId, GoodsCommentAddRequest goodsReplyAddRequest);

    void deleteGoods(Long goodsId, Long loggedInUserId);

    void updateGoods(Long loggedInUserId,Long goodsId,GoodsUpdateRequest goodsUpdateRequest);

    void deleteGoodsComment(Long loggedInUserId,Long goodsId,Long goodsCommentId);

    void updateGoodsComment(Long loggedInUserId, Long goodsId,Long goodsCommentId, GoodsCommentUpdateRequest goodsCommentUpdateRequest);

    void likeGoods(Long loggedInUserId,Long goodsId);

    Page<FavoriteGoodsResponse> getFavoriteGoods(Long loggedInUserId, Pageable pageable);
}
