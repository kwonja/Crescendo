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

import static com.sokpulee.crescendo.domain.feed.entity.QFeedCommentLike.feedCommentLike;

public class FeedCommentCustomRepositoryImpl implements FeedCommentCustomRepository{

    private final JPAQueryFactory queryFactory;

    public FeedCommentCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Page<FeedCommentResponse> findFeedComments(Long userId,Long feedId, Pageable pageable) {
        QFeed feed = QFeed.feed;
        QUser user = QUser.user;
        QFeedComment feedComment = QFeedComment.feedComment;

        JPAQuery<FeedComment> query = queryFactory
                .select(feedComment)
                .from(feedComment)
                .leftJoin(feedComment.user, user)
                .where(feedComment.feed.feedId.eq(feedId).and(feedComment.parentFeedComment.isNull()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<FeedCommentResponse> feedComments = query.fetch().stream()
                .map(f -> {
                    Boolean isLike = userId != null ? queryFactory
                            .select(feedCommentLike.count())
                            .from(feedCommentLike)
                            .where(feedCommentLike.feedComment.eq(f).and(feedCommentLike.user.id.eq(userId)))
                            .fetchOne() > 0 : false;

                    return new FeedCommentResponse(
                            f.getUser().getId(),
                            f.getUser().getProfilePath(),
                            f.getUser().getNickname(),
                            f.getLikeCnt(),
                            isLike,
                            f.getContent(),
                            f.getReplyCnt()
                    );
                })
                .collect(Collectors.toList());

        Long total = Optional.ofNullable(queryFactory
                .select(feedComment.count())
                .from(feedComment)
                .fetchOne()).orElse(0L);

        return new PageImpl<>(feedComments, pageable, total);
    }
}
