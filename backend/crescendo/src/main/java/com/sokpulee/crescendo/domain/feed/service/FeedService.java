package com.sokpulee.crescendo.domain.feed.service;

import com.sokpulee.crescendo.domain.feed.dto.request.FeedAddRequest;
import com.sokpulee.crescendo.domain.feed.dto.request.FeedCommentAddRequest;
import com.sokpulee.crescendo.domain.feed.dto.request.FeedCommentUpdateRequest;
import com.sokpulee.crescendo.domain.feed.dto.request.FeedUpdateRequest;
import com.sokpulee.crescendo.domain.feed.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedService {
    void addFeed(Long loggedInUserId, FeedAddRequest feedAddRequest);

    void addFeedComment(Long loggedInUserId, Long feedId ,FeedCommentAddRequest feedCommentAddRequest);

    void addFeedReply(Long loggedInUserId, Long feedId, Long feedCommentId, FeedCommentAddRequest feedReplyAddRequest);

    void deleteFeed(Long feedId, Long loggedInUserId);

    void updateFeed(Long loggedInUserId,Long feedId, FeedUpdateRequest feedUpdateRequest);

    void deleteFeedComment(Long loggedInUserId,Long feedId,Long feedCommentId);

    void updateFeedComment(Long loggedInUserId, Long feedId, Long feedCommentId, FeedCommentUpdateRequest feedCommentUpdateRequest);

    void likeFeed(Long loggedInUserId,Long feedId);

    void likeFeedComment(Long loggedInUserId,Long feedCommentId);

    Page<FeedResponse> getFeed(Long loggedInUserId,Long idolGroupId, Pageable pageable);

    FeedDetailResponse getFeedDetail(Long loggedInUserId, Long feedId);

    Page<FeedCommentResponse> getFeedComment(Long loggedInUserId, Long feedId, Pageable pageable);

    Page<FeedReplyResponse> getFeedReply(Long loggedInUserId,Long feedId,Long feedCommentId,Pageable pageable);

    Page<FavoriteFeedResponse> getFavoriteFeed(Long loggedInUserId, Pageable pageable);
}


