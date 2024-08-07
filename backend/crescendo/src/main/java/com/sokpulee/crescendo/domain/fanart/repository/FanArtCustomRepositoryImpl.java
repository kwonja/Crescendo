package com.sokpulee.crescendo.domain.fanart.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sokpulee.crescendo.domain.fanart.dto.request.FanArtSearchCondition;
import com.sokpulee.crescendo.domain.fanart.dto.response.FanArtResponse;
import com.sokpulee.crescendo.domain.fanart.dto.response.FavoriteFanArtResponse;
import com.sokpulee.crescendo.domain.fanart.dto.response.MyFanArtResponse;
import com.sokpulee.crescendo.domain.fanart.entity.FanArt;
import com.sokpulee.crescendo.domain.fanart.entity.QFanArt;
import com.sokpulee.crescendo.domain.fanart.entity.QFanArtImage;
import com.sokpulee.crescendo.domain.fanart.entity.QFanArtLike;
import com.sokpulee.crescendo.domain.idol.entity.QIdolGroup;
import com.sokpulee.crescendo.domain.user.entity.QUser;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FanArtCustomRepositoryImpl implements FanArtCustomRepository{
    private final JPAQueryFactory queryFactory;

    public FanArtCustomRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<FanArtResponse> findFanArts(Long loggedInUserId, Long idolGroupId, Pageable pageable, FanArtSearchCondition condition) {
        QFanArt fanArt = QFanArt.fanArt;
        QUser user = QUser.user;
        QFanArtImage fanArtImage = QFanArtImage.fanArtImage;
        QFanArtLike fanArtLike = QFanArtLike.fanArtLike;
        QIdolGroup idolGroup = QIdolGroup.idolGroup;

        // 검색 조건 추가
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(fanArt.idolGroup.id.eq(idolGroupId));

        if (condition != null) {
            if (condition.getTitle() != null && !condition.getTitle().isEmpty()) {
                booleanBuilder.and(fanArt.title.containsIgnoreCase(condition.getTitle()));
            }
            if (condition.getNickname() != null && !condition.getNickname().isEmpty()) {
                booleanBuilder.and(user.nickname.containsIgnoreCase(condition.getNickname()));
            }
            if (condition.getContent() != null && !condition.getContent().isEmpty()) {
                booleanBuilder.and(fanArt.content.containsIgnoreCase(condition.getContent()));
            }
        }

        // 페이징 및 기본 정보 조회
        List<FanArt> fanArts = queryFactory
                .selectFrom(fanArt)
                .leftJoin(fanArt.user, user).fetchJoin()
                .where(booleanBuilder)
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
                            Optional.ofNullable(f.getLikeCnt()).orElse(0),
                            isLike,
                            imagePaths,
                            f.getContent(),
                            Optional.ofNullable(f.getCommentCnt()).orElse(0),
                            f.getCreatedAt(),
                            f.getLastModified(),
                            f.getTitle()
                    );
                })
                .toList();

        Long total = Optional.ofNullable(queryFactory
                .select(fanArt.count())
                .from(fanArt)
                .where(booleanBuilder)
                .fetchOne()).orElse(0L);

        return new PageImpl<>(fanArtResponses, pageable, total);
    }


    @Override
    public Page<FavoriteFanArtResponse> findFavoriteFanArt(Long loggedInUserId, Pageable pageable) {
        QFanArt fanArt = QFanArt.fanArt;
        QUser user = QUser.user;
        QFanArtImage fanArtImage = QFanArtImage.fanArtImage;
        QFanArtLike fanArtLike = QFanArtLike.fanArtLike;

        List<FanArt> query = queryFactory
                .selectFrom(fanArt)
                .leftJoin(fanArt.user, user).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();

        List<FavoriteFanArtResponse> favoriteFanArtResponses = query.stream()
                .map(f -> {
                    Boolean isLike = loggedInUserId != null ? Optional.ofNullable(queryFactory
                            .select(fanArtLike.count())
                            .from(fanArtLike)
                            .where(fanArtLike.fanArt.eq(f).and(fanArtLike.user.id.eq(loggedInUserId)))
                            .fetchOne()).orElse(0L) > 0 : false;

                    if (!isLike) {
                        return null; // isLike가 false이면 null 반환
                    }


                    List<String> imagePaths = queryFactory
                            .select(fanArtImage.imagePath)
                            .from(fanArtImage)
                            .where(fanArtImage.fanArt.eq(f))
                            .fetch();


                    return new FavoriteFanArtResponse(
                            f.getFanArtId(),
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
                            f.getTitle()
                    );
                })
                .filter(Objects::nonNull) // null 값을 제거
                .toList();

        Long total = Optional.ofNullable(queryFactory
                .select(fanArt.count())
                .from(fanArt)
                .fetchOne()).orElse(0L);

        return new PageImpl<>(favoriteFanArtResponses, pageable, total);
    }

    @Override
    public Page<MyFanArtResponse> findMyFanArts(Long loggedInUserId, Pageable pageable) {

        QFanArt fanArt = QFanArt.fanArt;
        QUser user = QUser.user;
        QFanArtImage fanArtImage = QFanArtImage.fanArtImage;
        QFanArtLike fanArtLike = QFanArtLike.fanArtLike;

        // 페이징 및 기본 정보 조회
        List<FanArt> fanArtList = queryFactory
                .selectFrom(fanArt)
                .leftJoin(fanArt.user, user).fetchJoin()
                .where(user.id.eq(loggedInUserId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();

        // 피드 응답 변환
        List<MyFanArtResponse> fanArtResponses = fanArtList.stream()
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


                    return new MyFanArtResponse(
                            f.getFanArtId(),
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
                            f.getTitle()
                    );
                })
                .toList();

        Long total = Optional.ofNullable(queryFactory
                .select(fanArt.count())
                .from(fanArt)
                .where(user.id.eq(loggedInUserId))
                .fetchOne()).orElse(0L);

        return new PageImpl<>(fanArtResponses, pageable, total);
    }

}
