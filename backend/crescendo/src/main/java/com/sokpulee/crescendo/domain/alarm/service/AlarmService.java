package com.sokpulee.crescendo.domain.alarm.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface AlarmService {
    SseEmitter connect(Long userId);
}
