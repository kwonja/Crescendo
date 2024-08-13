package com.sokpulee.crescendo.domain.goods.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sokpulee.crescendo.domain.feed.dto.response.GetFeedByUserIdResponse;
import com.sokpulee.crescendo.domain.feed.entity.*;
import com.sokpulee.crescendo.domain.follow.entity.QFollow;
import com.sokpulee.crescendo.domain.goods.dto.request.GoodsSearchCondition;
import com.sokpulee.crescendo.domain.goods.dto.response.FavoriteGoodsResponse;
import com.sokpulee.crescendo.domain.goods.dto.response.GetGoodsByUserIdResponse;
import com.sokpulee.crescendo.domain.goods.dto.response.GoodsResponse;
import com.sokpulee.crescendo.domain.goods.dto.response.MyGoodsResponse;
import com.sokpulee.crescendo.domain.goods.entity.Goods;
import com.sokpulee.crescendo.domain.goods.entity.QGoods;
import com.sokpulee.crescendo.domain.goods.entity.QGoodsImage;
import com.sokpulee.crescendo.domain.goods.entity.QGoodsLike;
import com.sokpulee.crescendo.domain.idol.entity.QIdolGroup;
import com.sokpulee.crescendo.domain.user.entity.QUser;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
                .select(goods)
                .from(goodsLike)
                .join(goodsLike.goods, goods)  // 일반 조인 사용
                .join(goodsLike.user, user)
                .where(goodsLike.user.id.eq(loggedInUserId))
                .orderBy(goods.createdAt.desc())
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
        QIdolGroup idolGroup = QIdolGroup.idolGroup;
        QFollow follow = QFollow.follow; // Assuming there's a follow entity

        // 검색 조건 추가
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(goods.idolGroup.id.eq(idolGroup.id));



        // 페이징 및 기본 정보 조회
        List<Goods> goodsList = queryFactory
                .selectFrom(goods)
                .leftJoin(goods.user, user).fetchJoin()
                .where(user.id.eq(loggedInUserId))
                .orderBy(goods.createdAt.desc())
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
    public Page<GoodsResponse> findGoods(Long loggedInUserId, Long idolGroupId, Pageable pageable, GoodsSearchCondition condition) {

        QGoods goods = QGoods.goods;
        QUser user = QUser.user;
        QGoodsImage goodsImage = QGoodsImage.goodsImage;
        QGoodsLike goodsLike = QGoodsLike.goodsLike;
        QFollow follow = QFollow.follow; // Assuming there's a follow entity

        // 검색 조건 추가
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(goods.idolGroup.id.eq(idolGroupId));

        if (condition != null) {
            if (condition.getTitle() != null && !condition.getTitle().isEmpty()) {
                booleanBuilder.and(goods.title.containsIgnoreCase(condition.getTitle()));
            }
            if (condition.getNickname() != null && !condition.getNickname().isEmpty()) {
                booleanBuilder.and(user.nickname.containsIgnoreCase(condition.getNickname()));
            }
            if (condition.getContent() != null && !condition.getContent().isEmpty()) {
                booleanBuilder.and(goods.content.containsIgnoreCase(condition.getContent()));
            }
        }

        // Sort by followed users
        if (condition != null && Boolean.TRUE.equals(condition.getSortByFollowed()) && loggedInUserId != null) {
            booleanBuilder.and(goods.user.id.in(
                    JPAExpressions.select(follow.follower.id)
                            .from(follow)
                            .where(follow.following.id.eq(loggedInUserId))
            ));
        }


        // 기본 정보 조회
        JPAQuery<Goods> query = queryFactory
                .selectFrom(goods)
                .leftJoin(goods.user, user).fetchJoin()
                .leftJoin(goods.idolGroup).fetchJoin()
                .where(booleanBuilder)
                .distinct();
        // 기본 정렬: createdAt 최신순
        query.orderBy(goods.createdAt.desc());

        // Sort by liked feeds
        if (condition != null && Boolean.TRUE.equals(condition.getSortByLiked())) {
            query.orderBy(goods.likeCnt.desc(),goods.createdAt.desc());
        }else{
            query.orderBy(goods.createdAt.desc());
        }

        // 페이징 추가
        query.offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Goods> goodsList = query.fetch();


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
                .where(booleanBuilder)
                .fetchOne()).orElse(0L);

        return new PageImpl<>(goodsResponses, pageable, total);
    }

    @Override
    public Page<GetGoodsByUserIdResponse> findGoodsByUserId(Long userId, Pageable pageable) {
        QGoods goods = QGoods.goods;
        QUser user = QUser.user;
        QGoodsImage goodsImage = QGoodsImage.goodsImage;
        QGoodsLike goodsLike = QGoodsLike.goodsLike;
        QIdolGroup idolGroup = QIdolGroup.idolGroup;

        // 페이징 및 기본 정보 조회
        List<Goods> goodsList = queryFactory
                .selectFrom(goods)
                .leftJoin(goods.user, user).fetchJoin()
                .where(user.id.eq(userId))
                .orderBy(goods.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();

        // 피드 응답 변환
        List<GetGoodsByUserIdResponse> goodsResponse = goodsList.stream()
                .map(f -> {
                    Boolean isLike = userId != null ? Optional.ofNullable(queryFactory
                            .select(goodsLike.count())
                            .from(goodsLike)
                            .where(goodsLike.goods.eq(f).and(goodsLike.user.id.eq(userId)))
                            .fetchOne()).orElse(0L) > 0 : false;

                    List<String> imagePaths = queryFactory
                            .select(goodsImage.imagePath)
                            .from(goodsImage)
                            .where(goodsImage.goods.eq(f))
                            .fetch();

                    return new GetGoodsByUserIdResponse(
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
                            Optional.ofNullable(f.getCommentCnt()).orElse(0),
                            f.getIdolGroup().getName(),
                            f.getIdolGroup().getId()
                            );
                })
                .collect(Collectors.toList());

        Long total = Optional.ofNullable(queryFactory
                .select(goods.count())
                .from(goods)
                .where(user.id.eq(userId))
                .fetchOne()).orElse(0L);

        return new PageImpl<>(goodsResponse, pageable, total);
    }
}
