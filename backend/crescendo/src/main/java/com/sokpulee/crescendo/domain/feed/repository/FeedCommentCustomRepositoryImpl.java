package com.sokpulee.crescendo.domain.feed.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sokpulee.crescendo.domain.feed.dto.response.FeedCommentResponse;
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

public class FeedCommentCustomRepositoryImpl implements FeedCommentCustomRepository{

    private final JPAQueryFactory queryFactory;

    public FeedCommentCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Page<FeedCommentResponse> findFeedComments(Long userId, Pageable pageable) {
        QFeed feed = QFeed.feed;
        QUser user = QUser.user;
        QFeedComment feedComment = QFeedComment.feedComment;

        JPAQuery<FeedComment> query = queryFactory
                .select(feedComment)
                .from(feedComment)
                .leftJoin(feedComment.user, user)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<FeedCommentResponse> feeds = query.fetch().stream()
                .map(f -> {
//                    Boolean isLike = userId != null ? queryFactory
//                            .select(feedLike.count())
//                            .from(feedLike)
//                            .where(feedLike.feed.eq(f).and(feedLike.user.id.eq(userId)))
//                            .fetchOne() > 0 : false;

                    return new FeedCommentResponse(
                            f.getUser().getId(),
                            f.getUser().getProfilePath(),
                            f.getUser().getNickname(),
                            f.getLikeCnt(),
                            false,
                            f.getContent(),
                            1
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
