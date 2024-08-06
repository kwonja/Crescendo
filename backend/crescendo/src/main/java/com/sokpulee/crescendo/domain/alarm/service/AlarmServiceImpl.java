package com.sokpulee.crescendo.domain.alarm.service;

import com.sokpulee.crescendo.domain.alarm.dto.response.GetAlarmResponse;
import com.sokpulee.crescendo.domain.alarm.entity.Alarm;
import com.sokpulee.crescendo.domain.alarm.repository.alarm.AlarmRepository;
import com.sokpulee.crescendo.domain.alarm.repository.EmitterRepository;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.domain.user.repository.UserRepository;
import com.sokpulee.crescendo.global.exception.custom.AlarmNotFoundException;
import com.sokpulee.crescendo.global.exception.custom.UnAuthorizedAccessException;
import com.sokpulee.crescendo.global.exception.custom.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Transactional
@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    // 기본 타임아웃 설정
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;
    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;

    @Override
    public SseEmitter connect(Long userId) {
        SseEmitter emitter = createEmitter(userId);

        sendToClient(userId, "Connected! [userId=" + userId + "]");
        return emitter;
    }

    @Override
    public Page<GetAlarmResponse> getAlarms(Long loggedInUserId, Pageable pageable) {
        return alarmRepository.findAllByUserId(loggedInUserId, pageable);
    }

    @Override
    public long countUnreadAlarmsByUserId(Long loggedInUserId) {
        return alarmRepository.countByUserIdAndIsReadFalse(loggedInUserId);
    }

    @Override
    public void readAlarm(Long loggedInUserId, Long alarmId) {

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(AlarmNotFoundException::new);

        if(!alarm.getUser().getId().equals(user.getId())) {
            throw new UnAuthorizedAccessException();
        }

        alarm.readAlarm();
    }

    @Override
    public void deleteAlarm(Long loggedInUserId, Long alarmId) {

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(AlarmNotFoundException::new);

        if(!alarm.getUser().getId().equals(user.getId())) {
            throw new UnAuthorizedAccessException();
        }

        alarmRepository.delete(alarm);
    }

    private void sendToClient(Long id, Object data) {
        SseEmitter emitter = emitterRepository.get(id);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("connect").data(data));
            } catch (IOException exception) {
                emitterRepository.deleteById(id);
                emitter.completeWithError(exception);
            }
        }
    }

    private SseEmitter createEmitter(Long userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, emitter);

        emitter.onCompletion(() -> emitterRepository.deleteById(userId));
        emitter.onTimeout(() -> emitterRepository.deleteById(userId));

        return emitter;
    }
}
