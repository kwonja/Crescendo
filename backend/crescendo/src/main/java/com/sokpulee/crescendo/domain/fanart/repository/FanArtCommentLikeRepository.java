package com.sokpulee.crescendo.domain.fanart.repository;

import com.sokpulee.crescendo.domain.fanart.entity.FanArtCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FanArtCommentLikeRepository extends JpaRepository<FanArtCommentLike,Long> {
}
