package com.sokpulee.crescendo.domain.alarm.controller;

import com.sokpulee.crescendo.domain.alarm.entity.Alarm;
import com.sokpulee.crescendo.domain.alarm.service.AlarmService;
import com.sokpulee.crescendo.global.auth.annotation.AuthPrincipal;
import com.sokpulee.crescendo.global.exception.custom.AuthenticationRequiredException;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Alarm", description = "알림 관련 API")
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping(value="/connect/{user-id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(@PathVariable("user-id") Long userId) {
        return ResponseEntity.status(OK).body(alarmService.connect(userId));
    }

}
