package com.sokpulee.crescendo.domain.goods.repository;

import com.sokpulee.crescendo.domain.goods.dto.response.FavoriteGoodsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GoodsCustomRepository {
    Page<FavoriteGoodsResponse> findFavoriteGoods(Long loggedInUserId, Pageable pageable);
}
