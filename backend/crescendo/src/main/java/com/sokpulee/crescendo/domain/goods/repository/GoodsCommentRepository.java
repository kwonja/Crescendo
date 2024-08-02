package com.sokpulee.crescendo.domain.goods.repository;

import com.sokpulee.crescendo.domain.goods.entity.GoodsComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GoodsCommentRepository extends JpaRepository<GoodsComment,Long> {
}
