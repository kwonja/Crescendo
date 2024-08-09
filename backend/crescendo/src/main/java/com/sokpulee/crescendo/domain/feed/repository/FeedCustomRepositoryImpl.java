package com.sokpulee.crescendo.domain.feed.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sokpulee.crescendo.domain.feed.dto.request.FeedSearchCondition;
import com.sokpulee.crescendo.domain.feed.dto.response.FavoriteFeedResponse;
import com.sokpulee.crescendo.domain.feed.dto.response.FeedResponse;
import com.sokpulee.crescendo.domain.feed.dto.response.MyFeedResponse;
import com.sokpulee.crescendo.domain.feed.entity.*;
import com.sokpulee.crescendo.domain.follow.entity.QFollow;
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

public class FeedCustomRepositoryImpl implements FeedCustomRepository {

    private final JPAQueryFactory queryFactory;

    public FeedCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<FeedResponse> findFeeds(Long userId, Long idolGroupId, Pageable pageable, FeedSearchCondition condition) {
        QFeed feed = QFeed.feed;
        QUser user = QUser.user;
        QFeedImage feedImage = QFeedImage.feedImage;
        QFeedLike feedLike = QFeedLike.feedLike;
        QFeedHashtag feedHashtag = QFeedHashtag.feedHashtag;
        QIdolGroup idolGroup = QIdolGroup.idolGroup;
        QFollow follow = QFollow.follow; // Assuming there's a follow entity

        // 검색 조건 추가
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(feed.idolGroup.id.eq(idolGroupId));

        if (condition != null) {
            if (condition.getNickname() != null && !condition.getNickname().isEmpty()) {
                booleanBuilder.and(user.nickname.containsIgnoreCase(condition.getNickname()));
            }
            if (condition.getContent() != null && !condition.getContent().isEmpty()) {
                booleanBuilder.and(feed.content.containsIgnoreCase(condition.getContent()));
            }
        }

        // Sort by followed users
        if (condition != null && Boolean.TRUE.equals(condition.getSortByFollowed()) && userId != null) {
            booleanBuilder.and(feed.user.id.in(
                    JPAExpressions.select(follow.follower.id)
                            .from(follow)
                            .where(follow.following.id.eq(userId))
            ));
        }

        // 기본 정보 조회
        JPAQuery<Feed> query = queryFactory
                .selectFrom(feed)
                .leftJoin(feed.user, user).fetchJoin()
                .leftJoin(feed.idolGroup, idolGroup).fetchJoin()
                .where(booleanBuilder)
                .distinct();
        // 기본 정렬: createdAt 최신순
        query.orderBy(feed.createdAt.desc());

        // Sort by liked feeds
        if (condition != null && Boolean.TRUE.equals(condition.getSortByLiked())) {
            query.orderBy(feed.likeCnt.desc(),feed.createdAt.desc());
        }else{
            query.orderBy(feed.createdAt.desc());
        }

        // 페이징 추가
        query.offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Feed> feedList = query.fetch();

        // 피드 응답 변환
        List<FeedResponse> feeds = feedList.stream()
                .map(f -> {
                    Boolean isLike = userId != null ? Optional.ofNullable(queryFactory
                            .select(feedLike.count())
                            .from(feedLike)
                            .where(feedLike.feed.eq(f).and(feedLike.user.id.eq(userId)))
                            .fetchOne()).orElse(0L) > 0 : false;

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
                .where(booleanBuilder)
                .fetchOne()).orElse(0L);

        return new PageImpl<>(feeds, pageable, total);
    }


    @Override
    public Page<FavoriteFeedResponse> findFavoriteFeeds(Long loggedInUserId, Pageable pageable) {
        QFeed feed = QFeed.feed;
        QUser user = QUser.user;
        QFeedImage feedImage = QFeedImage.feedImage;
        QFeedLike feedLike = QFeedLike.feedLike;
        QFeedHashtag feedHashtag = QFeedHashtag.feedHashtag;

        List<Feed> query = queryFactory
                .select(feed)
                .from(feedLike)
                .join(feedLike.feed, feed)  // 일반 조인 사용
                .join(feedLike.user, user)
                .where(feedLike.user.id.eq(loggedInUserId))
                .orderBy(feed.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();


        List<FavoriteFeedResponse> favoriteFeedResponses = query.stream()
                .map(f -> {
                    boolean isLike = loggedInUserId != null ? Optional.ofNullable(queryFactory
                            .select(feedLike.count())
                            .from(feedLike)
                            .where(feedLike.feed.eq(f).and(feedLike.user.id.eq(loggedInUserId)))
                            .fetchOne()).orElse(0L) > 0 : false;

                    if (!isLike) {
                        return null; // isLike가 false이면 null 반환
                    }


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

                    return new FavoriteFeedResponse(
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
                .filter(Objects::nonNull) // null 값을 제거
                .collect(Collectors.toList());

        Long total = Optional.ofNullable(queryFactory
                .select(feed.count())
                .from(feed)
                .fetchOne()).orElse(0L);

        return new PageImpl<>(favoriteFeedResponses, pageable, total);

    }

    @Override
    public Page<MyFeedResponse> findMyFeeds(Long loggedInUserId, Pageable pageable) {
        QFeed feed = QFeed.feed;
        QUser user = QUser.user;
        QFeedImage feedImage = QFeedImage.feedImage;
        QFeedLike feedLike = QFeedLike.feedLike;
        QFeedHashtag feedHashtag = QFeedHashtag.feedHashtag;
        QIdolGroup idolGroup = QIdolGroup.idolGroup;

        // 페이징 및 기본 정보 조회
        List<Feed> feedList = queryFactory
                .selectFrom(feed)
                .leftJoin(feed.user, user).fetchJoin()
                .where(user.id.eq(loggedInUserId))
                .orderBy(feed.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();

        // 피드 응답 변환
        List<MyFeedResponse> feeds = feedList.stream()
                .map(f -> {
                    Boolean isLike = loggedInUserId != null ? Optional.ofNullable(queryFactory
                            .select(feedLike.count())
                            .from(feedLike)
                            .where(feedLike.feed.eq(f).and(feedLike.user.id.eq(loggedInUserId)))
                            .fetchOne()).orElse(0L) > 0 : false;

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

                    return new MyFeedResponse(
                            f.getFeedId(),
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
                            tags
                    );
                })
                .collect(Collectors.toList());

        Long total = Optional.ofNullable(queryFactory
                .select(feed.count())
                .from(feed)
                .where(user.id.eq(loggedInUserId))
                .fetchOne()).orElse(0L);

        return new PageImpl<>(feeds, pageable, total);
    }
}
