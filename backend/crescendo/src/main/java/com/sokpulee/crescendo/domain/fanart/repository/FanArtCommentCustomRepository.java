package com.sokpulee.crescendo.domain.fanart.repository;

import com.sokpulee.crescendo.domain.fanart.dto.response.FanArtCommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FanArtCommentCustomRepository {

    Page<FanArtCommentResponse> findFanArtComments(Long userId, Long fanArtId, Pageable pageable);

}
