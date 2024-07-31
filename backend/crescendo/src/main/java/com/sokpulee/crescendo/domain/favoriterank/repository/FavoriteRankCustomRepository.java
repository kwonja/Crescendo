package com.sokpulee.crescendo.domain.favoriterank.repository;

import com.sokpulee.crescendo.domain.favoriterank.dto.request.FavoriteRanksSearchCondition;
import com.sokpulee.crescendo.domain.favoriterank.dto.response.FavoriteRankResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoriteRankCustomRepository {

    Page<FavoriteRankResponse> findFavoriteRank(Long userId, FavoriteRanksSearchCondition condition, Pageable pageable);
}
