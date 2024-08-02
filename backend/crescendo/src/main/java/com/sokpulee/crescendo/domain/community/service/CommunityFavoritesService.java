package com.sokpulee.crescendo.domain.community.service;

public interface CommunityFavoritesService {

    public void toggleFavorite(Long idolGroupId, Long loggedInUserId);
}
