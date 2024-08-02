package com.sokpulee.crescendo.domain.feed.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sokpulee.crescendo.domain.feed.dto.response.FeedResponse;
import com.sokpulee.crescendo.domain.feed.entity.QFeed;
import com.sokpulee.crescendo.domain.feed.entity.QFeedHashtag;
import com.sokpulee.crescendo.domain.feed.entity.QFeedImage;
import com.sokpulee.crescendo.domain.feed.entity.QFeedLike;
import com.sokpulee.crescendo.domain.user.entity.QUser;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class FeedCustomRepositoryImpl implements FeedCustomRepository {

    private final JPAQueryFactory queryFactory;

    public FeedCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<FeedResponse> findFeeds(Long userId, Pageable pageable) {
        QFeed feed = QFeed.feed;
        QUser user = QUser.user;
        QFeedImage feedImage = QFeedImage.feedImage;
        QFeedLike like = QFeedLike.feedLike;
        QFeedHashtag tag = QFeedHashtag.feedHashtag;
        return null;
    }

//    @Override
//    public Page<FeedResponse> findFeeds(Long userId, Pageable pageable) {
//        QFeed feed = QFeed.feed;
//        QUser user = QUser.user;
//        QFeedImage feedImage = QFeedImage.feedImage;
//        QFeedLike like = QFeedLike.feedLike;
//
//
//
//        List<FeedResponse> feeds = queryFactory
//                .select(Projections.constructor(FeedResponse.class,
//                        feed.feedId,
//                        user.id,
//                        user.profilePath,
//                        user.nickname,
//                        feed.createdAt,
//                        feed.lastModified,
//                        feed.likeCnt,
//                        userId != null ? queryFactory.select(like.count())
//                                .from(like)
//                                .where(like.feed.eq(feed).and(like.user.id.eq(userId)))
//                                .exists() : null,
//                        feed.imageList,
//                        feed.content,
//                        feed.commentCnt,
//                        feed.hashtagList
//                ))
//                .from(feed)
//                .leftJoin(feed.user, user)
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        Long total = Optional.ofNullable(queryFactory
//                .select(feed.count())
//                .from(feed)
//                .fetchOne()).orElse(0L);
//
//        return new PageImpl<>(feeds, pageable, total);
//    }





}
