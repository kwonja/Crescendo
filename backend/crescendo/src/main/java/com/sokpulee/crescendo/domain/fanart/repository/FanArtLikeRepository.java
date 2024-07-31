package com.sokpulee.crescendo.domain.fanart.repository;

import com.sokpulee.crescendo.domain.fanart.entity.FanArtLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FanArtLikeRepository extends JpaRepository<FanArtLike,Long> {
}
