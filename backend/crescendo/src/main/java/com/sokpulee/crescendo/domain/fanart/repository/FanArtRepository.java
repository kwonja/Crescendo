package com.sokpulee.crescendo.domain.fanart.repository;

import com.sokpulee.crescendo.domain.fanart.entity.FanArt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FanArtRepository extends JpaRepository<FanArt,Long> {
}
