package com.sokpulee.crescendo.domain.goods.repository;

import com.sokpulee.crescendo.domain.goods.entity.GoodsComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsCommentRepository extends JpaRepository<GoodsComment,Long> {
}
