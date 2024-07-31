package com.sokpulee.crescendo.domain.feed.service;

import com.sokpulee.crescendo.domain.feed.dto.request.FeedAddRequest;

public interface FeedService {
    void addFeed(Long loggedInUserId, FeedAddRequest feedAddRequest);
}
