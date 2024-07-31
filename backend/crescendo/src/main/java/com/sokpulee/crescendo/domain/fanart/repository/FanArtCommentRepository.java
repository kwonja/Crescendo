package com.sokpulee.crescendo.domain.fanart.repository;

import com.sokpulee.crescendo.domain.fanart.entity.FanArtComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FanArtCommentRepository extends JpaRepository<FanArtComment,Long> {
}
