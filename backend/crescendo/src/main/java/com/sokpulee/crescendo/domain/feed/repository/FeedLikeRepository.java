package com.sokpulee.crescendo.domain.feed.repository;

import com.sokpulee.crescendo.domain.feed.entity.Feed;
import com.sokpulee.crescendo.domain.feed.entity.FeedLike;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.global.auth.annotation.AuthPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {

    Optional<FeedLike> findByFeedAndUser(Feed feed, User user);
}
