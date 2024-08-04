package com.sokpulee.crescendo.domain.dm.repository.dmgroup;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sokpulee.crescendo.domain.dm.dto.response.DmGroupResponseDto;
import com.sokpulee.crescendo.domain.dm.entity.QDmGroup;
import com.sokpulee.crescendo.domain.dm.entity.QDmMessage;
import com.sokpulee.crescendo.domain.dm.entity.QDmParticipants;
import com.sokpulee.crescendo.domain.user.entity.QUser;
import com.sokpulee.crescendo.domain.user.entity.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

public class DMGroupRepositoryImpl implements DMGroupRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public DMGroupRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
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

    public List<DmGroupResponseDto> findDmGroupsWithLastMessage(Long userId) {
        QDmGroup dmGroup = QDmGroup.dmGroup;
        QDmMessage dmMessage = QDmMessage.dmMessage;
        QDmParticipants dmParticipants = QDmParticipants.dmParticipants;
        QUser user = QUser.user;

        return queryFactory.select(Projections.constructor(
                        DmGroupResponseDto.class,
                        dmGroup.id,
                        user.id,
                        user.profilePath,
                        user.nickname,
                        dmMessage.content,
                        dmMessage.createdAt
                ))
                .from(dmGroup)
                .leftJoin(dmGroup.dmParticipantList, dmParticipants)
                .leftJoin(dmParticipants.user, user)
                .leftJoin(dmGroup.dmMessageList, dmMessage)
                .where(dmParticipants.user.id.eq(userId)
                        .and(dmMessage.id.in(
                                JPAExpressions.select(dmMessage.id.max())
                                        .from(dmMessage)
                                        .where(dmMessage.dmGroup.id.eq(dmGroup.id))
                        ))
                )
                .fetch();
    }
}
