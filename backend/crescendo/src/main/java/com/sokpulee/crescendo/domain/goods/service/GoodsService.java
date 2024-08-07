package com.sokpulee.crescendo.domain.goods.service;

import com.sokpulee.crescendo.domain.goods.dto.request.GoodsAddRequest;
import com.sokpulee.crescendo.domain.goods.dto.request.GoodsCommentAddRequest;
import com.sokpulee.crescendo.domain.goods.dto.request.GoodsCommentUpdateRequest;
import com.sokpulee.crescendo.domain.goods.dto.request.GoodsUpdateRequest;
import com.sokpulee.crescendo.domain.goods.dto.response.*;
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

    void likeGoodsComment(Long loggedInUserId,Long goodsCommentId);

    Page<FavoriteGoodsResponse> getFavoriteGoods(Long loggedInUserId, Pageable pageable);

    Page<MyGoodsResponse> getMyGoods(Long loggedInUserId,Pageable pageable);

    Page<GoodsResponse> getGoods(Long loggedInUserId,Long idolGroupId,Pageable pageable);

    Page<GoodsCommentResponse> getGoodsComment(Long loggedInUserId,Long goodsId,Pageable pageable);

    Page<GoodsReplyResponse> getGoodsReply(Long loggedInUserId,Long goodsId,Long goodsCommentId,Pageable pageable);

    GoodsDetailResponse getGoodsDetail(Long loggedInUserId, Long goodsId);
}
