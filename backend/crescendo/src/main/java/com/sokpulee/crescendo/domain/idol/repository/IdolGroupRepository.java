package com.sokpulee.crescendo.domain.idol.repository;

import com.sokpulee.crescendo.domain.idol.entity.IdolGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IdolGroupRepository extends JpaRepository<IdolGroup, Long> {

    List<IdolGroup> findByNameContainingOrderByNameAsc(String keyword);

    List<IdolGroup> findAllByOrderByNameAsc();

    Page<IdolGroup> findByNameContaining(String keyword, Pageable pageable);
}
