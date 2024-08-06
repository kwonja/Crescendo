package com.sokpulee.crescendo.domain.alarm.service;

import com.sokpulee.crescendo.domain.alarm.dto.response.GetAlarmResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface AlarmService {
    SseEmitter connect(Long userId);

    Page<GetAlarmResponse> getAlarms(Long loggedInUserId, Pageable pageable);

    long countUnreadAlarmsByUserId(Long loggedInUserId);
}
