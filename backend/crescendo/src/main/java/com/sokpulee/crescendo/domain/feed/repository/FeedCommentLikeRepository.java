package com.sokpulee.crescendo.domain.feed.repository;

import com.sokpulee.crescendo.domain.feed.entity.FeedCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedCommentLikeRepository extends JpaRepository<FeedCommentLike, Long> {
}
