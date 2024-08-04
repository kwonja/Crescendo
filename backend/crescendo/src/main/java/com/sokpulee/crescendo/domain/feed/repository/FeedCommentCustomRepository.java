package com.sokpulee.crescendo.domain.feed.repository;

import com.sokpulee.crescendo.domain.feed.dto.response.FeedCommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedCommentCustomRepository {
    Page<FeedCommentResponse> findFeedComments(Long userId, Pageable pageable);
}
