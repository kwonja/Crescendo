package com.sokpulee.crescendo.domain.feed.repository;

import com.sokpulee.crescendo.domain.feed.dto.response.FeedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedCustomRepository {
    Page<FeedResponse> findFeeds(Long userId,Pageable pageable);
}
