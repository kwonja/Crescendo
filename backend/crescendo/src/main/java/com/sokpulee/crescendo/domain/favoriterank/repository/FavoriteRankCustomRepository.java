package com.sokpulee.crescendo.domain.favoriterank.repository;

import com.sokpulee.crescendo.domain.favoriterank.dto.FavoriteRankBestPhotoDto;
import com.sokpulee.crescendo.domain.favoriterank.dto.request.FavoriteRanksSearchCondition;
import com.sokpulee.crescendo.domain.favoriterank.dto.response.FavoriteRankResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface FavoriteRankCustomRepository {

    Page<FavoriteRankResponse> findFavoriteRank(Long userId, FavoriteRanksSearchCondition condition, Pageable pageable);
    List<FavoriteRankBestPhotoDto> findBestRankedPhotos();
}
