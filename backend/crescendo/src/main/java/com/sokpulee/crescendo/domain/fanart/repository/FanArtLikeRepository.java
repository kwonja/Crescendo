package com.sokpulee.crescendo.domain.fanart.repository;

import com.sokpulee.crescendo.domain.fanart.entity.FanArt;
import com.sokpulee.crescendo.domain.fanart.entity.FanArtLike;
import com.sokpulee.crescendo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FanArtLikeRepository extends JpaRepository<FanArtLike,Long> {

    Optional<FanArtLike> findByFanArtAndUser(FanArt fanArt, User user);
}
