package com.sokpulee.crescendo.domain.fanart.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sokpulee.crescendo.domain.fanart.dto.response.FanArtCommentResponse;
import com.sokpulee.crescendo.domain.fanart.dto.response.FanArtReplyResponse;
import com.sokpulee.crescendo.domain.fanart.entity.FanArtComment;
import com.sokpulee.crescendo.domain.fanart.entity.QFanArt;
import com.sokpulee.crescendo.domain.fanart.entity.QFanArtComment;
import com.sokpulee.crescendo.domain.fanart.entity.QFanArtCommentLike;
import com.sokpulee.crescendo.domain.user.entity.QUser;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public class FanArtCommentCustomRepositoryImpl implements FanArtCommentCustomRepository {

    private final JPAQueryFactory queryFactory;

    public FanArtCommentCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<FanArtCommentResponse> findFanArtComments(Long userId, Long fanArtId, Pageable pageable) {
        QFanArt fanArt = QFanArt.fanArt;
        QUser user = QUser.user;
        QFanArtComment fanArtComment = QFanArtComment.fanArtComment;
        QFanArtCommentLike fanArtCommentLike = QFanArtCommentLike.fanArtCommentLike;

        JPAQuery<FanArtComment> query = queryFactory
                .select(fanArtComment)
                .from(fanArtComment)
                .leftJoin(fanArtComment.user, user)
                .where(fanArtComment.fanArt.fanArtId.eq(fanArtId).and(fanArtComment.parentFanArtComment.isNull()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<FanArtCommentResponse> fanArtCommentResponses = query.fetch().stream()
                .map(f -> {
                    Boolean isLike = userId != null ? queryFactory
                            .select(fanArtCommentLike.count())
                            .from(fanArtCommentLike)
                            .where(fanArtCommentLike.fanArtComment.eq(f).and(fanArtCommentLike.user.id.eq(userId)))
                            .fetchOne() > 0 : false;

                    return new FanArtCommentResponse(
                            f.getFanArtCommentId(),
                            f.getUser().getId(),
                            f.getUser().getProfilePath(),
                            f.getUser().getNickname(),
                            f.getLikeCnt(),
                            isLike,
                            f.getContent(),
                            f.getReplyCnt(),
                            f.getCreatedAt(),
                            f.getLastModified()
                    );
                })
                .toList();

        Long total = Optional.ofNullable(queryFactory
                .select(fanArtComment.count())
                .from(fanArtComment)
                .fetchOne()).orElse(0L);

        return new PageImpl<>(fanArtCommentResponses, pageable, total);
    }

    @Override
    public Page<FanArtReplyResponse> findFanArtReply(Long loggedInUserId, Long fanArtId, Long fanArtCommentId, Pageable pageable) {
        QFanArt fanArt = QFanArt.fanArt;
        QUser user = QUser.user;
        QFanArtComment fanArtComment = QFanArtComment.fanArtComment;
        QFanArtCommentLike fanArtCommentLike = QFanArtCommentLike.fanArtCommentLike;

        JPAQuery<FanArtComment> query = queryFactory
                .select(fanArtComment)
                .from(fanArtComment)
                .leftJoin(fanArtComment.user, user)
                .where(fanArtComment.fanArt.fanArtId.eq(fanArtId).and(fanArtComment.parentFanArtComment.isNotNull()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<FanArtReplyResponse> fanArtReplyResponses = query.fetch().stream()
                .map(f -> {
                    Boolean isLike = loggedInUserId != null ? queryFactory
                            .select(fanArtCommentLike.count())
                            .from(fanArtCommentLike)
                            .where(fanArtCommentLike.fanArtComment.eq(f).and(fanArtCommentLike.user.id.eq(loggedInUserId)))
                            .fetchOne() > 0 : false;

                    return new FanArtReplyResponse(
                            f.getUser().getId(),
                            f.getUser().getProfilePath(),
                            f.getUser().getNickname(),
                            f.getLikeCnt(),
                            isLike,
                            f.getContent(),
                            f.getCreatedAt(),
                            f.getLastModified()
                    );
                })
                .toList();

        Long total = Optional.ofNullable(queryFactory
                .select(fanArtComment.count())
                .from(fanArtComment)
                .fetchOne()).orElse(0L);

        return new PageImpl<>(fanArtReplyResponses, pageable, total);
    }
}
