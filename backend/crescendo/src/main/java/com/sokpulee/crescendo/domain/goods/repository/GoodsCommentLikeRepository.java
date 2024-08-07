package com.sokpulee.crescendo.domain.goods.repository;

import com.sokpulee.crescendo.domain.goods.entity.GoodsComment;
import com.sokpulee.crescendo.domain.goods.entity.GoodsCommentLike;
import com.sokpulee.crescendo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoodsCommentLikeRepository extends JpaRepository<GoodsCommentLike, Long> {
    Optional<GoodsCommentLike> findByGoodsCommentAndUser(GoodsComment goodsComment, User user);
}
