package com.sokpulee.idolcrawling.crawling.repository;

import com.sokpulee.idolcrawling.crawling.entity.Idol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdolRepository extends JpaRepository<Idol, Long> {
}
