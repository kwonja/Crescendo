package com.sokpulee.crescendo.domain.favoriterank.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sokpulee.crescendo.domain.favoriterank.dto.FavoriteRankBestPhotoDto;
import com.sokpulee.crescendo.domain.favoriterank.dto.request.FavoriteRanksSearchCondition;
import com.sokpulee.crescendo.domain.favoriterank.dto.response.FavoriteRankBestPhotoResponse;
import com.sokpulee.crescendo.domain.favoriterank.dto.response.FavoriteRankResponse;
import com.sokpulee.crescendo.domain.favoriterank.entity.QFavoriteRank;
import com.sokpulee.crescendo.domain.favoriterank.entity.QFavoriteRankVoting;
import com.sokpulee.crescendo.domain.idol.entity.QIdol;
import com.sokpulee.crescendo.domain.idol.entity.QIdolGroup;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FavoriteRankRepositoryImpl implements FavoriteRankCustomRepository {

    private final JPAQueryFactory queryFactory;

    public FavoriteRankRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<FavoriteRankResponse> findFavoriteRank(Long userId, FavoriteRanksSearchCondition condition, Pageable pageable) {
        QFavoriteRank favoriteRank = QFavoriteRank.favoriteRank;
        QFavoriteRankVoting favoriteRankVoting = QFavoriteRankVoting.favoriteRankVoting;
        QIdol idol = QIdol.idol;
        QIdolGroup idolGroup = QIdolGroup.idolGroup;

        JPAQuery<FavoriteRankResponse> query;

        if (userId != null) {
            // 서브쿼리를 사용하여 'isLike' 여부 계산
            BooleanExpression isLikeExpression = queryFactory.selectFrom(favoriteRankVoting)
                    .where(favoriteRankVoting.favoriteRank.eq(favoriteRank)
                            .and(favoriteRankVoting.user.id.eq(userId)))
                    .exists();

            // 메인 쿼리 작성
            query = queryFactory
                    .select(Projections.constructor(FavoriteRankResponse.class,
                            idol.idolGroup.name.as("idolGroupName"),
                            idol.name.as("idolName"),
                            favoriteRank.user.id,
                            favoriteRank.user.nickname,
                            favoriteRank.user.profilePath,
                            favoriteRank.id,
                            favoriteRank.favoriteIdolImagePath,
                            favoriteRankVoting.id.countDistinct().as("likeCnt"),
                            isLikeExpression.as("isLike"),
                            favoriteRank.createdAt));
        } else {
            // 'isLike' 컬럼을 제외한 메인 쿼리 작성
            query = queryFactory
                    .select(Projections.constructor(FavoriteRankResponse.class,
                            idol.idolGroup.name.as("idolGroupName"),
                            idol.name.as("idolName"),
                            favoriteRank.user.id,
                            favoriteRank.user.nickname,
                            favoriteRank.user.profilePath,
                            favoriteRank.id,
                            favoriteRank.favoriteIdolImagePath,
                            favoriteRankVoting.id.countDistinct().as("likeCnt"),
                            favoriteRank.createdAt));
        }

        query.from(favoriteRank)
                .leftJoin(favoriteRank.idol, idol)
                .leftJoin(idol.idolGroup, idolGroup)
                .leftJoin(favoriteRankVoting).on(favoriteRank.id.eq(favoriteRankVoting.favoriteRank.id));

        BooleanExpression conditionExpression = null;
        if (condition.getIdolId() != null) {
            conditionExpression = idol.id.eq(condition.getIdolId());
        } else if (condition.getIdolGroupId() != null) {
            conditionExpression = idol.idolGroup.id.eq(condition.getIdolGroupId());
        }

        if (conditionExpression != null) {
            query.where(conditionExpression);
        }

        query.groupBy(favoriteRank.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        if (condition.isSortByVotes()) {
            query.orderBy(favoriteRankVoting.id.countDistinct().desc());
        } else {
            query.orderBy(favoriteRank.createdAt.desc());
        }

        List<FavoriteRankResponse> favoriteRanks = query.fetch();

        Long total = Optional.ofNullable(
                queryFactory.select(favoriteRank.count())
                        .from(favoriteRank)
                        .where(conditionExpression)
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(favoriteRanks, pageable, total);
    }

    @Override
    public List<FavoriteRankBestPhotoDto> findBestRankedPhotos() {
        QFavoriteRank favoriteRank = QFavoriteRank.favoriteRank;
        QFavoriteRankVoting favoriteRankVoting = QFavoriteRankVoting.favoriteRankVoting;

        List<Tuple> subQuery = queryFactory
                .select(favoriteRank.idol.id, favoriteRank.id, favoriteRankVoting.id.count())
                .from(favoriteRank)
                .join(favoriteRankVoting).on(favoriteRank.id.eq(favoriteRankVoting.favoriteRank.id))
                .groupBy(favoriteRank.idol.id, favoriteRank.id)
                .fetch();

        Map<Long, Tuple> bestRankByIdol = subQuery.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(favoriteRank.idol.id),
                        tuple -> tuple,
                        (tuple1, tuple2) -> tuple1.get(favoriteRankVoting.id.count()).compareTo(tuple2.get(favoriteRankVoting.id.count())) > 0 ? tuple1 : tuple2
                ));

        List<Long> bestRankIds = bestRankByIdol.values().stream()
                .map(tuple -> tuple.get(favoriteRank.id))
                .collect(Collectors.toList());

        return queryFactory.select(
                Projections.constructor(FavoriteRankBestPhotoDto.class,
                        favoriteRank.idol.id,
                        favoriteRank.idol.name,
                        favoriteRank.favoriteIdolImagePath))
                .from(favoriteRank)
                .join(favoriteRank.idol)
                .where(favoriteRank.id.in(bestRankIds))
                .fetch();
    }
}
