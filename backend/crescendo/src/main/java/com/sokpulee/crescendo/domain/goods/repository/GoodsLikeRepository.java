package com.sokpulee.crescendo.domain.goods.repository;

import com.sokpulee.crescendo.domain.goods.entity.Goods;
import com.sokpulee.crescendo.domain.goods.entity.GoodsLike;
import com.sokpulee.crescendo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoodsLikeRepository extends JpaRepository<GoodsLike,Long> {
    Optional<GoodsLike> findByGoodsAndUser(Goods goods, User user);
}
