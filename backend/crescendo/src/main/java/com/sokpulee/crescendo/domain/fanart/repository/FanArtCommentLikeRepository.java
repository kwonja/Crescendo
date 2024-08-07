package com.sokpulee.crescendo.domain.fanart.repository;

import com.sokpulee.crescendo.domain.fanart.entity.FanArtComment;
import com.sokpulee.crescendo.domain.fanart.entity.FanArtCommentLike;
import com.sokpulee.crescendo.domain.feed.entity.FeedCommentLike;
import com.sokpulee.crescendo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FanArtCommentLikeRepository extends JpaRepository<FanArtCommentLike,Long> {

    Optional<FanArtCommentLike> findByFanArtCommentAndUser(FanArtComment fanArtComment, User user);
}
