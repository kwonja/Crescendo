package com.sokpulee.crescendo.domain.community.service;

import com.sokpulee.crescendo.domain.community.dto.response.FavoritesGetResponse;
import com.sokpulee.crescendo.domain.community.dto.response.IdolGroupDetailResponse;
import com.sokpulee.crescendo.domain.community.dto.response.IdolGroupGetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommunityService {

    void toggleFavorite(Long idolGroupId, Long loggedInUserId);

    Page<FavoritesGetResponse> getFavorites(Long loggedInUserId, Pageable pageable);

    Page<IdolGroupGetResponse> getIdolGroups(String keyword, Pageable pageable);

    IdolGroupDetailResponse getIdolGroupDetail(Long loggedInUserId, Long idolGroupId);
}
