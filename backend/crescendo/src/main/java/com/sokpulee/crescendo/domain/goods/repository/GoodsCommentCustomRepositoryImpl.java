package com.sokpulee.crescendo.domain.goods.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sokpulee.crescendo.domain.goods.dto.response.GoodsCommentResponse;
import com.sokpulee.crescendo.domain.goods.dto.response.GoodsReplyResponse;
import com.sokpulee.crescendo.domain.goods.entity.*;
import com.sokpulee.crescendo.domain.user.entity.QUser;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class GoodsCommentCustomRepositoryImpl implements GoodsCommentCustomRepository{

    private final JPAQueryFactory queryFactory;

    public GoodsCommentCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Page<GoodsCommentResponse> findGoodsComment(Long loggedInUserId, Long goodsId, Pageable pageable) {
        QGoods goods = QGoods.goods;
        QUser user = QUser.user;
        QGoodsComment goodsComment = QGoodsComment.goodsComment;
        QGoodsCommentLike goodsCommentLike = QGoodsCommentLike.goodsCommentLike;

        JPAQuery<GoodsComment> query = queryFactory
                .select(goodsComment)
                .from(goodsComment)
                .leftJoin(goodsComment.user, user)
                .where(goodsComment.goods.goodsId.eq(goodsId).and(goodsComment.parentGoodsComment.isNull()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<GoodsCommentResponse> goodsCommentResponses = query.fetch().stream()
                .map(f -> {
                    Boolean isLike = loggedInUserId != null ? queryFactory
                            .select(goodsCommentLike.count())
                            .from(goodsCommentLike)
                            .where(goodsCommentLike.goodsComment.eq(f).and(goodsCommentLike.user.id.eq(loggedInUserId)))
                            .fetchOne() > 0 : false;

                    return new GoodsCommentResponse(
                            f.getGoodsCommentId(),
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
                .select(goodsComment.count())
                .from(goodsComment)
                .fetchOne()).orElse(0L);

        return new PageImpl<>(goodsCommentResponses, pageable, total);
    }

    @Override
    public Page<GoodsReplyResponse> findGoodsReply(Long loggedInUserId, Long goodsId, Long goodsCommentId, Pageable pageable) {

        QGoods goods = QGoods.goods;
        QUser user = QUser.user;
        QGoodsComment goodsComment = QGoodsComment.goodsComment;
        QGoodsCommentLike goodsCommentLike = QGoodsCommentLike.goodsCommentLike;

        JPAQuery<GoodsComment> query = queryFactory
                .select(goodsComment)
                .from(goodsComment)
                .where(goodsComment.parentGoodsComment.goodsCommentId.eq(goodsCommentId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<GoodsReplyResponse> goodsReplyResponses = query.fetch().stream()
                .map(f -> {
                    Boolean isLike = loggedInUserId != null ? queryFactory
                            .select(goodsCommentLike.count())
                            .from(goodsCommentLike)
                            .where(goodsCommentLike.goodsComment.eq(f).and(goodsCommentLike.user.id.eq(loggedInUserId)))
                            .fetchOne() > 0 : false;

                    return new GoodsReplyResponse(
                            f.getUser().getId(),
                            f.getUser().getProfilePath(),
                            f.getUser().getNickname(),
                            f.getLikeCnt(),
                            isLike,
                            f.getContent(),
                            f.getCreatedAt(),
                            f.getLastModified(),
                            f.getGoodsCommentId()
                    );
                })
                .toList();

        Long total = Optional.ofNullable(queryFactory
                .select(goodsComment.count())
                .from(goodsComment)
                .fetchOne()).orElse(0L);

        return new PageImpl<>(goodsReplyResponses, pageable, total);
    }
}
