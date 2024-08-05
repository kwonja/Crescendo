package com.sokpulee.crescendo.global.scheduler;

import com.sokpulee.crescendo.domain.user.repository.EmailAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class EmailAuthCleanupScheduler {

    private final EmailAuthRepository emailAuthRepository;

    @Scheduled(fixedRate = 60000)
    public void deleteExpiredEmailAuths() {
        emailAuthRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }
}
