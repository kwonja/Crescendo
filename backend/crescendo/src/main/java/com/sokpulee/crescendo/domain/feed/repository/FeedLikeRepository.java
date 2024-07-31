package com.sokpulee.crescendo.domain.feed.repository;

import com.sokpulee.crescendo.domain.feed.entity.FeedLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {
}
