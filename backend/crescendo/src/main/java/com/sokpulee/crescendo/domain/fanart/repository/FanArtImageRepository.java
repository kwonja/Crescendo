package com.sokpulee.crescendo.domain.fanart.repository;

import com.sokpulee.crescendo.domain.fanart.entity.FanArtImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FanArtImageRepository extends JpaRepository<FanArtImage,Long> {
}
