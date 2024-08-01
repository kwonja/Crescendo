package com.sokpulee.crescendo.domain.dm.repository.dmgroup;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sokpulee.crescendo.domain.dm.dto.response.DmGroupResponseDto;
import com.sokpulee.crescendo.domain.dm.entity.QDmGroup;
import com.sokpulee.crescendo.domain.dm.entity.QDmMessage;
import com.sokpulee.crescendo.domain.dm.entity.QDmParticipants;
import com.sokpulee.crescendo.domain.user.entity.QUser;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

public class DMGroupRepositoryImpl implements DMGroupRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public DMGroupRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<DmGroupResponseDto> findDmGroupsByUserId(Long userId, Pageable pageable) {
        QDmGroup dmGroup = QDmGroup.dmGroup;
        QDmParticipants dmParticipants = QDmParticipants.dmParticipants;
        QUser user = QUser.user;
        QDmMessage dmMessage = QDmMessage.dmMessage;

        List<DmGroupResponseDto> results = queryFactory
                .select(Projections.constructor(DmGroupResponseDto.class,
                        dmGroup.id,
                        user.id,
                        user.profilePath,
                        user.nickname,
                        dmMessage.content,
                        dmMessage.createdAt))
                .from(dmGroup)
                .join(dmGroup.dmParticipantList, dmParticipants)
                .join(dmParticipants.user, user)
                .leftJoin(dmMessage).on(dmMessage.dmGroup.id.eq(dmGroup.id))
                .where(dmParticipants.user.id.eq(userId))
                .orderBy(dmMessage.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(dmGroup.count())
                .from(dmGroup)
                .join(dmGroup.dmParticipantList, dmParticipants)
                .where(dmParticipants.user.id.eq(userId));

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    @Override
    public boolean existsByUserIdAndDmGroupId(Long loggedInUserId, Long dmGroupId) {
        QDmGroup dmGroup = QDmGroup.dmGroup;
        QDmParticipants dmParticipants = QDmParticipants.dmParticipants;

        Integer fetchOne = queryFactory
                .selectOne()
                .from(dmGroup)
                .join(dmGroup.dmParticipantList, dmParticipants)
                .where(dmGroup.id.eq(dmGroupId)
                        .and(dmParticipants.user.id.eq(loggedInUserId)))
                .fetchFirst();

        return fetchOne != null;
    }
}
