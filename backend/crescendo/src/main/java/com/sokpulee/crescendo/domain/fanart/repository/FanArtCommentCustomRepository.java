package com.sokpulee.crescendo.domain.fanart.repository;

import com.sokpulee.crescendo.domain.fanart.dto.response.FanArtCommentResponse;
import com.sokpulee.crescendo.domain.fanart.dto.response.FanArtReplyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FanArtCommentCustomRepository {

    Page<FanArtCommentResponse> findFanArtComments(Long userId, Long fanArtId, Pageable pageable);

    Page<FanArtReplyResponse> findFanArtReply(Long loggedInUserId,Long fanArtId,Long fanArtCommentId,Pageable pageable);
}
