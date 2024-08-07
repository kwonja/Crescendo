package com.sokpulee.crescendo.domain.goods.repository;

import com.sokpulee.crescendo.domain.goods.dto.response.GoodsCommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GoodsCommentCustomRepository {

    Page<GoodsCommentResponse> findGoodsComment(Long loggedInUserId, Long goodsId, Pageable pageable);
}
