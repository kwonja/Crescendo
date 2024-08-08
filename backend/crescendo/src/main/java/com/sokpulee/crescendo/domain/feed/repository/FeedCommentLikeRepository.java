package com.sokpulee.crescendo.domain.feed.repository;

import com.sokpulee.crescendo.domain.feed.entity.FeedComment;
import com.sokpulee.crescendo.domain.feed.entity.FeedCommentLike;
import com.sokpulee.crescendo.domain.feed.entity.FeedLike;
import com.sokpulee.crescendo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedCommentLikeRepository extends JpaRepository<FeedCommentLike, Long> {

    Optional<FeedCommentLike> findByFeedCommentAndUser(FeedComment feedComment, User user);
}
