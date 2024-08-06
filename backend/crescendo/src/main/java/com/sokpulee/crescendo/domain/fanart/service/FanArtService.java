package com.sokpulee.crescendo.domain.fanart.service;

import com.sokpulee.crescendo.domain.fanart.dto.request.FanArtAddRequest;
import com.sokpulee.crescendo.domain.fanart.dto.request.FanArtCommentAddRequest;
import com.sokpulee.crescendo.domain.fanart.dto.request.FanArtCommentUpdateRequest;
import com.sokpulee.crescendo.domain.fanart.dto.request.FanArtUpdateRequest;
import com.sokpulee.crescendo.domain.fanart.dto.response.FanArtResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FanArtService {
    void addFanArt(Long loggedInUserId, FanArtAddRequest fanArtAddRequest);

    void addFanArtComment(Long loggedInUserId, Long fanArtId, FanArtCommentAddRequest fanArtCommentAddRequest);

    void addFanArtReply(Long loggedInUserId, Long fanArtId, Long fanArtCommentId, FanArtCommentAddRequest fanArtReplyAddRequest);

    void deleteFanArt(Long fanArtId, Long loggedInUserId);

    void updateFanArt(Long loggedInUserId,Long fanArtId, FanArtUpdateRequest fanArtUpdateRequest);

    void deleteFanArtComment(Long loggedInUserId,Long fanArtId,Long fanArtCommentId);

    void updateFanArtComment(Long loggedInUserId, Long fanArtId, Long fanArtCommentId, FanArtCommentUpdateRequest fanArtCommentUpdateRequest);

    void likeFeed(Long loggedInUserId,Long fanArtId);

    Page<FanArtResponse> getFanArt(Long loggedInUserId, Long idolGroupId, Pageable pageable);
}
