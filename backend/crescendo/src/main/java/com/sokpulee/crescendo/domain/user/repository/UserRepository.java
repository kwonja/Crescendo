package com.sokpulee.crescendo.domain.user.repository;

import com.sokpulee.crescendo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
