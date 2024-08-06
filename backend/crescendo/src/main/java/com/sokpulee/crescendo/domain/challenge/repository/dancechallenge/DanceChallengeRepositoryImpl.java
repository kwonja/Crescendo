package com.sokpulee.crescendo.domain.challenge.repository.dancechallenge;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sokpulee.crescendo.domain.challenge.dto.response.GetDanceChallengeResponse;
import com.sokpulee.crescendo.domain.challenge.entity.QDanceChallenge;
import com.sokpulee.crescendo.domain.challenge.entity.QDanceChallengeJoin;
import com.sokpulee.crescendo.domain.user.entity.QUser;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class DanceChallengeRepositoryImpl implements DanceChallengeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public DanceChallengeRepositoryImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<GetDanceChallengeResponse> searchChallenges(String title, String sortBy, Pageable pageable) {
        QDanceChallenge danceChallenge = QDanceChallenge.danceChallenge;
        QUser user = QUser.user;
        QDanceChallengeJoin danceChallengeJoin = QDanceChallengeJoin.danceChallengeJoin;

        List<GetDanceChallengeResponse> results = queryFactory.select(
                Projections.constructor(GetDanceChallengeResponse.class,
                        danceChallenge.id,
                        danceChallenge.title,
                        danceChallenge.videoPath,
                        danceChallenge.createdAt,
                        danceChallenge.endAt,
                        user.id,
                        user.nickname,
                        danceChallengeJoin.count().intValue()
                ))
                .from(danceChallenge)
                .leftJoin(danceChallenge.user, user)
                .leftJoin(danceChallenge.danceChallengeJoins, danceChallengeJoin)
                .where(danceChallenge.title.containsIgnoreCase(title))
                .groupBy(danceChallenge.id, user.id)
                .orderBy(getOrderSpecifier(danceChallenge, danceChallengeJoin, sortBy))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Optional.ofNullable(
                queryFactory.select(danceChallenge.count())
                        .from(danceChallenge)
                        .where(danceChallenge.title.containsIgnoreCase(title))
                        .fetchOne()
                ).orElse(0L); ;

        return new PageImpl<>(results, pageable, total);
    }

    private OrderSpecifier<?> getOrderSpecifier(QDanceChallenge danceChallenge, QDanceChallengeJoin danceChallengeJoin, String sortBy) {
        if ("participants".equals(sortBy)) {
            return danceChallengeJoin.count().desc();
        } else {
            return danceChallenge.createdAt.desc();
        }
    }

}
