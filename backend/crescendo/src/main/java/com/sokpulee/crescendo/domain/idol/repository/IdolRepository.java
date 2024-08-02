package com.sokpulee.crescendo.domain.idol.repository;

import com.sokpulee.crescendo.domain.idol.entity.Idol;
import com.sokpulee.crescendo.domain.idol.entity.IdolGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IdolRepository extends JpaRepository<Idol, Long> {

    List<Idol> findByIdolGroup(IdolGroup idolGroup);
}
