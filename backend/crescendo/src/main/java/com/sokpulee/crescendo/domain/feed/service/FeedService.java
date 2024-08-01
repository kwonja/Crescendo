package com.sokpulee.crescendo.domain.feed.service;

import com.sokpulee.crescendo.domain.feed.dto.request.FeedAddRequest;
import com.sokpulee.crescendo.domain.feed.dto.request.FeedCommentAddRequest;
import com.sokpulee.crescendo.domain.feed.dto.request.FeedUpdateRequest;

public interface FeedService {
    void addFeed(Long loggedInUserId, FeedAddRequest feedAddRequest);

    void addFeedComment(Long loggedInUserId, Long feedId ,FeedCommentAddRequest feedCommentAddRequest);

    void addFeedReply(Long loggedInUserId, Long feedId, Long feedCommentId, FeedCommentAddRequest feedReplyAddRequest);

    void deleteFeed(Long feedId, Long loggedInUserId);

    void updateFeed(Long loggedInUserId,Long feedId, FeedUpdateRequest feedUpdateRequest);
}
