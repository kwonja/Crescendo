package com.sokpulee.crescendo.domain.dm.repository.dmmessage;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sokpulee.crescendo.domain.dm.dto.response.DmMessageResponseDto;
import com.sokpulee.crescendo.domain.dm.entity.QDmMessage;
import com.sokpulee.crescendo.domain.user.entity.QUser;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

public class DMMessageRepositoryImpl implements DMMessageRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public DMMessageRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<DmMessageResponseDto> findMessagesByDmGroupId(Long dmGroupId, Pageable pageable) {
        QDmMessage dmMessage = QDmMessage.dmMessage;
        QUser user = QUser.user;

        List<DmMessageResponseDto> results = queryFactory
                .select(Projections.constructor(DmMessageResponseDto.class,
                        dmMessage.id,
                        dmMessage.content,
                        dmMessage.createdAt,
                        user.id,
                        user.nickname,
                        user.profilePath))
                .from(dmMessage)
                .join(dmMessage.user, user)
                .where(dmMessage.dmGroup.id.eq(dmGroupId))
                .orderBy(dmMessage.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(dmMessage.count())
                .from(dmMessage)
                .where(dmMessage.dmGroup.id.eq(dmGroupId));

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }
}
