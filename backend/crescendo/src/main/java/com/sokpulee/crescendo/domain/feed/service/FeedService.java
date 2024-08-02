package com.sokpulee.crescendo.domain.feed.service;

import com.sokpulee.crescendo.domain.favoriterank.dto.response.FavoriteRankResponse;
import com.sokpulee.crescendo.domain.feed.dto.request.FeedAddRequest;
import com.sokpulee.crescendo.domain.feed.dto.request.FeedCommentAddRequest;
import com.sokpulee.crescendo.domain.feed.dto.request.FeedCommentUpdateRequest;
import com.sokpulee.crescendo.domain.feed.dto.request.FeedUpdateRequest;
import com.sokpulee.crescendo.domain.feed.dto.response.FeedResponse;
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

    Page<FeedResponse> getFeed(Long loggedInUserId, Pageable pageable);
}


