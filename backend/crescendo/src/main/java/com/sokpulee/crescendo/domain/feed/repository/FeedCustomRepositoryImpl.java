package com.sokpulee.crescendo.domain.feed.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.sokpulee.crescendo.domain.feed.dto.response.FeedResponse;
import com.sokpulee.crescendo.domain.feed.entity.*;
import com.sokpulee.crescendo.domain.user.entity.QUser;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        QFeedLike feedLike = QFeedLike.feedLike;
        QFeedHashtag feedHashtag = QFeedHashtag.feedHashtag;

        JPAQuery<Feed> query = queryFactory
                .select(feed)
                .from(feed)
                .leftJoin(feed.user, user)
                .leftJoin(feed.imageList, feedImage)
                .leftJoin(feed.hashtagList, feedHashtag)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<FeedResponse> feeds = query.fetch().stream()
                .map(f -> {
                    Boolean isLike = userId != null ? queryFactory
                            .select(feedLike.count())
                            .from(feedLike)
                            .where(feedLike.feed.eq(f).and(feedLike.user.id.eq(userId)))
                            .fetchOne() > 0 : false;

                    List<String> imagePaths = queryFactory
                            .select(feedImage.imagePath)
                            .from(feedImage)
                            .where(feedImage.feed.eq(f))
                            .fetch();

                    List<String> tags = queryFactory
                            .select(feedHashtag.tag)
                            .from(feedHashtag)
                            .where(feedHashtag.feed.eq(f))
                            .fetch();

                    return new FeedResponse(
                            f.getFeedId(),
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
                            tags
                    );
                })
                .collect(Collectors.toList());

        Long total = Optional.ofNullable(queryFactory
                .select(feed.count())
                .from(feed)
                .fetchOne()).orElse(0L);

        return new PageImpl<>(feeds, pageable, total);
    }
}
