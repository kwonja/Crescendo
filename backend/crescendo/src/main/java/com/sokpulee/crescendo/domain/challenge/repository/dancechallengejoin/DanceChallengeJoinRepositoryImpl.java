package com.sokpulee.crescendo.domain.challenge.repository.dancechallengejoin;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sokpulee.crescendo.domain.challenge.dto.response.GetDanceChallengeJoinResponse;
import com.sokpulee.crescendo.domain.challenge.entity.QDanceChallengeJoin;
import com.sokpulee.crescendo.domain.challenge.entity.QDanceChallengeJoinLike;
import com.sokpulee.crescendo.domain.user.entity.QUser;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class DanceChallengeJoinRepositoryImpl implements DanceChallengeJoinRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public DanceChallengeJoinRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<GetDanceChallengeJoinResponse> searchChallengeJoins(Long challengeId, String nickname, String sortBy, Long userId, Pageable pageable) {
        QDanceChallengeJoin danceChallengeJoin = QDanceChallengeJoin.danceChallengeJoin;
        QUser user = QUser.user;
        QDanceChallengeJoinLike danceChallengeJoinLike = QDanceChallengeJoinLike.danceChallengeJoinLike;

        List<GetDanceChallengeJoinResponse> results = null;
        if(userId != null) {
            results = queryFactory.select(
                            Projections.constructor(GetDanceChallengeJoinResponse.class,
                                    danceChallengeJoin.id,
                                    danceChallengeJoin.videoPath,
                                    danceChallengeJoin.score,
                                    user.id,
                                    user.nickname,
                                    danceChallengeJoinLike.count().intValue(),
                                    queryFactory.selectOne()
                                            .from(danceChallengeJoinLike)
                                            .where(danceChallengeJoinLike.danceChallengeJoin.eq(danceChallengeJoin)
                                                    .and(danceChallengeJoinLike.user.id.eq(userId)))
                                            .exists()
                            ))
                    .from(danceChallengeJoin)
                    .leftJoin(danceChallengeJoin.user, user)
                    .leftJoin(danceChallengeJoin.danceChallengeJoinLikes, danceChallengeJoinLike)
                    .where(danceChallengeJoin.danceChallenge.id.eq(challengeId)
                            .and(user.nickname.containsIgnoreCase(nickname)))
                    .groupBy(danceChallengeJoin.id, user.id)
                    .orderBy(getOrderSpecifier(danceChallengeJoin, danceChallengeJoinLike, sortBy))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        }
        else {
             results = queryFactory.select(
                            Projections.constructor(GetDanceChallengeJoinResponse.class,
                                    danceChallengeJoin.id,
                                    danceChallengeJoin.videoPath,
                                    danceChallengeJoin.score,
                                    user.id,
                                    user.nickname,
                                    danceChallengeJoinLike.count().intValue()
                            ))
                    .from(danceChallengeJoin)
                    .leftJoin(danceChallengeJoin.user, user)
                    .leftJoin(danceChallengeJoin.danceChallengeJoinLikes, danceChallengeJoinLike)
                    .where(danceChallengeJoin.danceChallenge.id.eq(challengeId)
                            .and(user.nickname.containsIgnoreCase(nickname)))
                    .groupBy(danceChallengeJoin.id, user.id)
                    .orderBy(getOrderSpecifier(danceChallengeJoin, danceChallengeJoinLike, sortBy))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        }


        long total = Optional.ofNullable(queryFactory.select(danceChallengeJoin.count())
                .from(danceChallengeJoin)
                .where(danceChallengeJoin.danceChallenge.id.eq(challengeId)
                        .and(user.nickname.containsIgnoreCase(nickname)))
                .fetchOne()).orElse(0L);

        return new PageImpl<>(results, pageable, total);
    }

    private OrderSpecifier<?> getOrderSpecifier(QDanceChallengeJoin danceChallengeJoin, QDanceChallengeJoinLike danceChallengeJoinLike, String sortBy) {
        if ("likeCnt".equals(sortBy)) {
            return danceChallengeJoinLike.count().desc();
        } else {
            return danceChallengeJoin.createdAt.desc();
        }
    }
}
