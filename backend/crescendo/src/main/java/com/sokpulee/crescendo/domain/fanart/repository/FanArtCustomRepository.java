package com.sokpulee.crescendo.domain.fanart.repository;

import com.sokpulee.crescendo.domain.fanart.dto.response.FanArtResponse;
import com.sokpulee.crescendo.domain.fanart.dto.response.FavoriteFanArtResponse;
import com.sokpulee.crescendo.domain.fanart.dto.response.MyFanArtResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FanArtCustomRepository {
    Page<FanArtResponse> findFanArts(Long loggedInUserId, Long idolGroupId, Pageable pageable);

    Page<FavoriteFanArtResponse> findFavoriteFanArt(Long loggedInUserId, Pageable pageable);

    Page<MyFanArtResponse> findMyFanArts(Long loggedInUserId,Pageable pageable);
}
