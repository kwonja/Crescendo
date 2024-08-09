package com.sokpulee.crescendo.domain.alarm.service;

import com.sokpulee.crescendo.domain.alarm.dto.response.GetAlarmResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface AlarmService {
    SseEmitter connect(Long userId);

    Page<GetAlarmResponse> getAlarms(Long loggedInUserId, Pageable pageable);

    long countUnreadAlarmsByUserId(Long loggedInUserId);

    void readAlarm(Long loggedInUserId, Long alarmId);

    void deleteAlarm(Long loggedInUserId, Long alarmId);

    void followAlarm(Long followingUserId, Long followerUserId, Long relatedId);

    void challengeJoinAlarm(String challengeName, Long challengeOrganizerId, Long challengeJoinerId, Long relatedId);

    void challengeJoinLikeAlarm(String challengeName, Long challengeJoinerId, Long challengeJoinLikedUserId, Long relatedId);

    void goodsLikeAlarm(String goodsTitle, Long goodsWriterId, Long goodsLikedUserId, Long relatedId);
}
