package com.sokpulee.crescendo.domain.fanart.service;

import com.sokpulee.crescendo.domain.fanart.dto.request.*;
import com.sokpulee.crescendo.domain.fanart.dto.response.*;
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

    void likeFanArt(Long loggedInUserId,Long fanArtId);

    void likeFanArtComment(Long loggedInUserId,Long fanArtCommentId);

    Page<FanArtResponse> getFanArt(Long loggedInUserId, Long idolGroupId, Pageable pageable, FanArtSearchCondition condition);

    Page<FavoriteFanArtResponse> getFavoriteFanArt(Long loggedInUserId,Pageable pageable);

    Page<MyFanArtResponse> getMyFanArt(Long loggedInUserId,Pageable pageable);

    Page<FanArtReplyResponse> getFanArtReply(Long loggedInUserId,Long fanArtId,Long fanArtCommentId,Pageable pageable);

    Page<FanArtCommentResponse> getFanArtComment(Long loggedInUserId,Long fanArtId,Pageable pageable);

    FanArtDetailResponse getFanArtDetail(Long loggedInUserId, Long fanArtId);
}
