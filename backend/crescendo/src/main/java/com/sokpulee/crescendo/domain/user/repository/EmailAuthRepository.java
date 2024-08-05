package com.sokpulee.crescendo.domain.user.repository;

import com.sokpulee.crescendo.domain.user.entity.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {

    void deleteByExpiresAtBefore(LocalDateTime now);
}
