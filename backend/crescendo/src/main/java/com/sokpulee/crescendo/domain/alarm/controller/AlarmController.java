package com.sokpulee.crescendo.domain.alarm.controller;

import com.sokpulee.crescendo.domain.alarm.dto.response.GetAlarmResponse;
import com.sokpulee.crescendo.domain.alarm.entity.Alarm;
import com.sokpulee.crescendo.domain.alarm.service.AlarmService;
import com.sokpulee.crescendo.global.auth.annotation.AuthPrincipal;
import com.sokpulee.crescendo.global.exception.custom.AuthenticationRequiredException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Alarm", description = "알림 관련 API")
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping(value="/connect/{user-id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "알림 연결", description = "SSE 연결")
    public ResponseEntity<SseEmitter> connect(@PathVariable("user-id") Long userId) {
        return ResponseEntity.status(OK).body(alarmService.connect(userId));
    }

    @GetMapping("/api/v1/alarm")
    @Operation(summary = "내 알림 조회", description = "내 알림 조회 API")
    public ResponseEntity<?> getAlarms(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<GetAlarmResponse> response = alarmService.getAlarms(loggedInUserId, pageable);

        return ResponseEntity.status(OK).body(response);
    }

    @GetMapping("/api/v1/alarm/count")
    @Operation(summary = "내 읽지 않은 알림 갯수 조회", description = "내 읽지 않은 알림 갯수 조회")
    public ResponseEntity<?> getAlarmCount(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId
    ) {

        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        return ResponseEntity.status(OK).body(alarmService.countUnreadAlarmsByUserId(loggedInUserId));
    }

    @PatchMapping("/api/v1/alarm/{alarm-id}/read")
    @Operation(summary = "알림 읽기", description = "알림 읽기 API")
    public ResponseEntity<?> readAlarm(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("alarm-id") Long alarmId
    ) {

        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        alarmService.readAlarm(loggedInUserId, alarmId);

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @DeleteMapping("/api/v1/alarm/{alarm-id}")
    @Operation(summary = "알림 삭제", description = "알림 삭제 API")
    public ResponseEntity<?> deleteAlarm(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("alarm-id") Long alarmId
    ) {

        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        alarmService.deleteAlarm(loggedInUserId, alarmId);

        return ResponseEntity.status(NO_CONTENT).build();
    }
}
