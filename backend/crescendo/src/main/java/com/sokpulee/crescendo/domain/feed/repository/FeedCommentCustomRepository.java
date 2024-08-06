package com.sokpulee.crescendo.domain.feed.repository;

import com.sokpulee.crescendo.domain.feed.dto.response.FeedCommentResponse;
import com.sokpulee.crescendo.domain.feed.dto.response.FeedReplyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedCommentCustomRepository {
    Page<FeedCommentResponse> findFeedComments(Long userId,Long feedId, Pageable pageable);

    Page<FeedReplyResponse> findFeedReply(Long loggedInUserId, Long feedId, Long feedCommentId, Pageable pageable);
}
