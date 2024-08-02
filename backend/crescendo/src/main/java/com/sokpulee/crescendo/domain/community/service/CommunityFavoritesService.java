package com.sokpulee.crescendo.domain.community.service;

import com.sokpulee.crescendo.domain.community.dto.response.FavoritesGetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommunityFavoritesService {

    public void toggleFavorite(Long idolGroupId, Long loggedInUserId);

    Page<FavoritesGetResponse> getFavorites(Long loggedInUserId, Pageable pageable);
}
