package com.sokpulee.crescendo.domain.goods.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sokpulee.crescendo.domain.goods.dto.response.FavoriteGoodsResponse;
import com.sokpulee.crescendo.domain.goods.entity.Goods;
import com.sokpulee.crescendo.domain.goods.entity.QGoods;
import com.sokpulee.crescendo.domain.goods.entity.QGoodsImage;
import com.sokpulee.crescendo.domain.goods.entity.QGoodsLike;
import com.sokpulee.crescendo.domain.user.entity.QUser;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GoodsCustomRepositoryImpl implements GoodsCustomRepository{

    private final JPAQueryFactory queryFactory;

    public GoodsCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<FavoriteGoodsResponse> findFavoriteGoods(Long loggedInUserId, Pageable pageable) {
        QGoods goods = QGoods.goods;
        QUser user = QUser.user;
        QGoodsImage goodsImage = QGoodsImage.goodsImage;
        QGoodsLike goodsLike = QGoodsLike.goodsLike;

        List<Goods> query = queryFactory
                .selectFrom(goods)
                .leftJoin(goods.user, user).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();

        List<FavoriteGoodsResponse> favoriteGoodsResponses = query.stream()
                .map(f -> {
                    Boolean isLike = loggedInUserId != null ? Optional.ofNullable(queryFactory
                            .select(goodsLike.count())
                            .from(goodsLike)
                            .where(goodsLike.goods.eq(f).and(goodsLike.user.id.eq(loggedInUserId)))
                            .fetchOne()).orElse(0L) > 0 : false;

                    if (!isLike) {
                        return null; // isLike가 false이면 null 반환
                    }


                    List<String> imagePaths = queryFactory
                            .select(goodsImage.imagePath)
                            .from(goodsImage)
                            .where(goodsImage.goods.eq(f))
                            .fetch();


                    return new FavoriteGoodsResponse(
                            f.getGoodsId(),
                            f.getUser().getId(),
                            f.getUser().getProfilePath(),
                            f.getUser().getNickname(),
                            f.getCreatedAt(),
                            f.getLastModified(),
                            f.getLikeCnt(),
                            isLike,
                            imagePaths,
                            f.getContent(),
                            f.getCommentCnt()
                    );
                })
                .filter(Objects::nonNull) // null 값을 제거
                .toList();

        Long total = Optional.ofNullable(queryFactory
                .select(goods.count())
                .from(goods)
                .fetchOne()).orElse(0L);

        return new PageImpl<>(favoriteGoodsResponses, pageable, total);
    }
}
