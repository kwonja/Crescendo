package com.sokpulee.crescendo.domain.user.repository;

import com.sokpulee.crescendo.domain.user.entity.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {
}
