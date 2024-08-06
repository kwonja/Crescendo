package com.sokpulee.crescendo.domain.fanart.repository;

import com.sokpulee.crescendo.domain.fanart.dto.response.FanArtResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FanArtCustomRepository {
    Page<FanArtResponse> findFanArts(Long loggedInUserId, Long idolGroupId, Pageable pageable);
}
