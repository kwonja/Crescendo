package com.sokpulee.crescendo.domain.dm.repository.dmgroup;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sokpulee.crescendo.domain.dm.dto.response.MyDmGroupResponseDto;
import com.sokpulee.crescendo.domain.dm.entity.*;
import com.sokpulee.crescendo.domain.user.entity.QUser;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.domain.user.repository.UserRepository;
import com.sokpulee.crescendo.global.exception.custom.UserNotFoundException;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DMGroupRepositoryImpl implements DMGroupRepositoryCustom {

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

    public List<MyDmGroupResponseDto> findDmGroupsWithLastMessage(Long userId) {
        QDmParticipants qDmParticipants = QDmParticipants.dmParticipants;
        QDmMessage qDmMessage = QDmMessage.dmMessage;
        QDmGroup qDmGroup = QDmGroup.dmGroup;
        QUser qUser = QUser.user;

        List<DmGroup> dmGroups = queryFactory.selectFrom(qDmGroup)
                .join(qDmGroup.dmParticipantList, qDmParticipants)
                .where(qDmParticipants.user.id.eq(userId))
                .fetch();

        List<MyDmGroupResponseDto> dmList = dmGroups.stream().map(dmGroup -> {
            DmParticipants opponent = queryFactory.selectFrom(qDmParticipants)
                    .where(qDmParticipants.dmGroup.eq(dmGroup)
                            .and(qDmParticipants.user.id.ne(userId)))
                    .fetchOne();

            Optional<DmMessage> lastMessageOpt = Optional.ofNullable(
                    queryFactory.selectFrom(qDmMessage)
                            .where(qDmMessage.dmGroup.eq(dmGroup))
                            .orderBy(qDmMessage.createdAt.desc())
                            .fetchFirst()
            );

            Long opponentId = opponent != null ? opponent.getUser().getId() : null;
            String opponentProfilePath = opponent != null ? opponent.getUser().getProfilePath() : null;
            String opponentNickName = opponent != null ? opponent.getUser().getNickname() : null;
            String lastChatting = lastMessageOpt.map(DmMessage::getContent).orElse(null);
            String lastChattingTime = lastMessageOpt.map(message -> message.getCreatedAt().toString()).orElse(null);

            return new MyDmGroupResponseDto(
                    dmGroup.getId(),
                    opponentId,
                    opponentProfilePath,
                    opponentNickName,
                    lastChatting,
                    lastChattingTime
            );
        }).toList();

        return dmList.stream()
                .sorted((d1, d2) -> d2.getLastChattingTime().compareTo(d1.getLastChattingTime()))
                .collect(Collectors.toList());
    }
}
