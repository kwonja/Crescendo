package com.sokpulee.crescendo.global.scheduler;

import com.sokpulee.crescendo.domain.user.repository.EmailAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@Component
@RequiredArgsConstructor
public class EmailAuthCleanupScheduler {

    private final EmailAuthRepository emailAuthRepository;

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void deleteExpiredEmailAuths() {
        emailAuthRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }
}