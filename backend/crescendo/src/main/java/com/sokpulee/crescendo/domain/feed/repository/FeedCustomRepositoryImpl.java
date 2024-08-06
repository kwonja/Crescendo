package com.sokpulee.crescendo.domain.feed.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAQuery;
import com.sokpulee.crescendo.domain.feed.dto.response.FavoriteFeedResponse;
import com.sokpulee.crescendo.domain.feed.dto.response.FeedResponse;
import com.sokpulee.crescendo.domain.feed.dto.response.MyFeedResponse;
import com.sokpulee.crescendo.domain.feed.entity.*;
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
    public Page<FeedResponse> findFeeds(Long userId,Long idolGroupId, Pageable pageable) {
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
                .where(feed.idolGroup.id.eq(idolGroupId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();

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
                .where(feed.idolGroup.id.eq(idolGroupId))
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
                .selectFrom(feed)
                .leftJoin(feed.user, user).fetchJoin()
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
