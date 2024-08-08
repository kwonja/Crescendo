package com.sokpulee.crescendo.domain.feed.repository;

import com.sokpulee.crescendo.domain.feed.dto.request.FeedSearchCondition;
import com.sokpulee.crescendo.domain.feed.dto.response.FavoriteFeedResponse;
import com.sokpulee.crescendo.domain.feed.dto.response.FeedResponse;
import com.sokpulee.crescendo.domain.feed.dto.response.MyFeedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedCustomRepository {
    Page<FeedResponse> findFeeds(Long userId, Long idolGroupId, Pageable pageable, FeedSearchCondition condition);

    Page<FavoriteFeedResponse> findFavoriteFeeds(Long loggedInUserId, Pageable pageable);

    Page<MyFeedResponse> findMyFeeds(Long loggedInUserId,Pageable pageable);
}
