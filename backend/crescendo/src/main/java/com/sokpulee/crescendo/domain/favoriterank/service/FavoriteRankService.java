package com.sokpulee.crescendo.domain.favoriterank.service;

import com.sokpulee.crescendo.domain.favoriterank.dto.request.FavoriteRankAddRequest;

public interface FavoriteRankService {

    void updateIdolProfileWithTopFavoriteImage();

    void registerFavoriteRank(Long loggedInUserId, FavoriteRankAddRequest favoriteRankAddRequest);
}
