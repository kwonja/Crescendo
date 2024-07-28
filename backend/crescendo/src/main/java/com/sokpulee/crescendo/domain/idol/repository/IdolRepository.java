package com.sokpulee.crescendo.domain.idol.repository;

import com.sokpulee.crescendo.domain.idol.entity.Idol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdolRepository extends JpaRepository<Idol, Long> {
}
