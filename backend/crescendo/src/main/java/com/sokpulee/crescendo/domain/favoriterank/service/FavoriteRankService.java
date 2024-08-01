package com.sokpulee.crescendo.domain.favoriterank.service;

import com.sokpulee.crescendo.domain.favoriterank.dto.request.FavoriteRankAddRequest;
import com.sokpulee.crescendo.domain.favoriterank.dto.request.FavoriteRanksSearchCondition;
import com.sokpulee.crescendo.domain.favoriterank.dto.response.FavoriteRankBestPhotoResponse;
import com.sokpulee.crescendo.domain.favoriterank.dto.response.FavoriteRankResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoriteRankService {

    void updateIdolProfileWithTopFavoriteImage();

    void registerFavoriteRank(Long loggedInUserId, FavoriteRankAddRequest favoriteRankAddRequest);

    Page<FavoriteRankResponse> getFavoriteRanks(Long userId, FavoriteRanksSearchCondition condition, Pageable pageable);

    void deleteFavoriteRank(Long loggedInUserId, Long favoriteRankId);

    FavoriteRankBestPhotoResponse getBestPhoto();

    void voteFavoriteRank(Long loggedInUserId, Long favoriteRankId);

}
