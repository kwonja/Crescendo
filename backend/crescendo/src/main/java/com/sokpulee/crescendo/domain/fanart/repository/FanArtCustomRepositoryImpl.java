package com.sokpulee.crescendo.domain.fanart.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sokpulee.crescendo.domain.fanart.dto.response.FanArtResponse;
import com.sokpulee.crescendo.domain.fanart.entity.FanArt;
import com.sokpulee.crescendo.domain.fanart.entity.QFanArt;
import com.sokpulee.crescendo.domain.fanart.entity.QFanArtImage;
import com.sokpulee.crescendo.domain.fanart.entity.QFanArtLike;
import com.sokpulee.crescendo.domain.feed.dto.response.FeedResponse;
import com.sokpulee.crescendo.domain.idol.entity.QIdolGroup;
import com.sokpulee.crescendo.domain.user.entity.QUser;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FanArtCustomRepositoryImpl implements FanArtCustomRepository{
    private final JPAQueryFactory queryFactory;

    public FanArtCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<FanArtResponse> findFanArts(Long loggedInUserId, Long idolGroupId, Pageable pageable) {
        QFanArt fanArt = QFanArt.fanArt;
        QUser user = QUser.user;
        QFanArtImage fanArtImage = QFanArtImage.fanArtImage;
        QFanArtLike fanArtLike = QFanArtLike.fanArtLike;
        QIdolGroup idolGroup = QIdolGroup.idolGroup;

        // 페이징 및 기본 정보 조회
        List<FanArt> fanArts = queryFactory
                .selectFrom(fanArt)
                .leftJoin(fanArt.user,user).fetchJoin()
                .where(fanArt.idolGroup.id.eq(idolGroupId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();

        //팬아트 응답 변환
        List<FanArtResponse> fanArtResponses = fanArts.stream()
                .map(f -> {
                    Boolean isLike = loggedInUserId != null ? Optional.ofNullable(queryFactory
                            .select(fanArtLike.count())
                            .from(fanArtLike)
                            .where(fanArtLike.fanArt.eq(f).and(fanArtLike.user.id.eq(loggedInUserId)))
                            .fetchOne()).orElse(0L) > 0 : false;

                    List<String> imagePaths = queryFactory
                            .select(fanArtImage.imagePath)
                            .from(fanArtImage)
                            .where(fanArtImage.fanArt.eq(f))
                            .fetch();

                    return new FanArtResponse(
                            f.getFanArtId(),
                            f.getUser().getId(),
                            f.getUser().getProfilePath(),
                            f.getUser().getNickname(),
                            f.getLikeCnt(),
                            isLike,
                            imagePaths,
                            f.getContent(),
                            f.getCommentCnt(),
                            f.getCreatedAt(),
                            f.getLastModified()
                    );
                })
                .toList();

        Long total = Optional.ofNullable(queryFactory
                .select(fanArt.count())
                .from(fanArt)
                .where(fanArt.idolGroup.id.eq(idolGroupId))
                .fetchOne()).orElse(0L);

        return new PageImpl<>(fanArtResponses, pageable, total);
    }

}
