package com.sokpulee.crescendo.domain.goods.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sokpulee.crescendo.domain.goods.dto.response.FavoriteGoodsResponse;
import com.sokpulee.crescendo.domain.goods.dto.response.GoodsResponse;
import com.sokpulee.crescendo.domain.goods.dto.response.MyGoodsResponse;
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
                            f.getCommentCnt(),
                            f.getTitle()
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

    @Override
    public Page<MyGoodsResponse> findMyGoods(Long loggedInUserId, Pageable pageable) {

        QGoods goods = QGoods.goods;
        QUser user = QUser.user;
        QGoodsImage goodsImage = QGoodsImage.goodsImage;
        QGoodsLike goodsLike = QGoodsLike.goodsLike;


        // 페이징 및 기본 정보 조회
        List<Goods> goodsList = queryFactory
                .selectFrom(goods)
                .leftJoin(goods.user, user).fetchJoin()
                .where(user.id.eq(loggedInUserId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();

        // 피드 응답 변환
        List<MyGoodsResponse> myGoodsResponses = goodsList.stream()
                .map(f -> {
                    Boolean isLike = loggedInUserId != null ? Optional.ofNullable(queryFactory
                            .select(goodsLike.count())
                            .from(goodsLike)
                            .where(goodsLike.goods.eq(f).and(goodsLike.user.id.eq(loggedInUserId)))
                            .fetchOne()).orElse(0L) > 0 : false;

                    List<String> imagePaths = queryFactory
                            .select(goodsImage.imagePath)
                            .from(goodsImage)
                            .where(goodsImage.goods.eq(f))
                            .fetch();


                    return new MyGoodsResponse(
                            f.getGoodsId(),
                            f.getUser().getId(),
                            f.getUser().getProfilePath(),
                            f.getUser().getNickname(),
                            f.getCreatedAt(),
                            f.getLastModified(),
                            Optional.ofNullable(f.getLikeCnt()).orElse(0), // 여기에서 null 체크
                            isLike,
                            imagePaths,
                            f.getContent(),
                            Optional.ofNullable(f.getCommentCnt()).orElse(0), // 여기에서 null 체크
                            f.getTitle()
                    );
                })
                .toList();

        Long total = Optional.ofNullable(queryFactory
                .select(goods.count())
                .from(goods)
                .where(user.id.eq(loggedInUserId))
                .fetchOne()).orElse(0L);

        return new PageImpl<>(myGoodsResponses, pageable, total);
    }

    @Override
    public Page<GoodsResponse> findGoods(Long loggedInUserId, Long idolGroupId, Pageable pageable) {

        QGoods goods = QGoods.goods;
        QUser user = QUser.user;
        QGoodsImage goodsImage = QGoodsImage.goodsImage;
        QGoodsLike goodsLike = QGoodsLike.goodsLike;


        // 페이징 및 기본 정보 조회
        List<Goods> goodsList = queryFactory
                .selectFrom(goods)
                .leftJoin(goods.user,user).fetchJoin()
                .where(goods.idolGroup.id.eq(idolGroupId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();

        //팬아트 응답 변환
        List<GoodsResponse> goodsResponses = goodsList.stream()
                .map(f -> {
                    Boolean isLike = loggedInUserId != null ? Optional.ofNullable(queryFactory
                            .select(goodsLike.count())
                            .from(goodsLike)
                            .where(goodsLike.goods.eq(f).and(goodsLike.user.id.eq(loggedInUserId)))
                            .fetchOne()).orElse(0L) > 0 : false;

                    List<String> imagePaths = queryFactory
                            .select(goodsImage.imagePath)
                            .from(goodsImage)
                            .where(goodsImage.goods.eq(f))
                            .fetch();

                    return new GoodsResponse(
                            f.getGoodsId(),
                            f.getUser().getId(),
                            f.getUser().getProfilePath(),
                            f.getUser().getNickname(),
                            Optional.ofNullable(f.getLikeCnt()).orElse(0),                            isLike,
                            imagePaths,
                            f.getContent(),
                            Optional.ofNullable(f.getCommentCnt()).orElse(0),                            f.getCreatedAt(),
                            f.getLastModified(),
                            f.getTitle()
                    );
                })
                .toList();

        Long total = Optional.ofNullable(queryFactory
                .select(goods.count())
                .from(goods)
                .where(goods.idolGroup.id.eq(idolGroupId))
                .fetchOne()).orElse(0L);

        return new PageImpl<>(goodsResponses, pageable, total);
    }
}
