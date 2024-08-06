package com.sokpulee.crescendo.domain.alarm.repository.alarm;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sokpulee.crescendo.domain.alarm.dto.response.GetAlarmResponse;
import com.sokpulee.crescendo.domain.alarm.entity.QAlarm;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class AlarmRepositoryImpl implements AlarmRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public AlarmRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<GetAlarmResponse> findAllByUserId(Long userId, Pageable pageable) {
        QAlarm alarm = QAlarm.alarm;

        List<GetAlarmResponse> alarms = queryFactory
                .select(Projections.constructor(GetAlarmResponse.class,
                        alarm.id.as("alarmId"),
                        alarm.alarmChannel.id.as("alarmChannelId"),
                        alarm.relatedId,
                        alarm.content,
                        alarm.isRead,
                        alarm.createdAt))
                .from(alarm)
                .where(alarm.user.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Optional.ofNullable(queryFactory
                .select(alarm.count())
                .from(alarm)
                .where(alarm.user.id.eq(userId))
                .fetchOne()).orElse(0L);

        return new PageImpl<>(alarms, pageable, total);
    }

}
