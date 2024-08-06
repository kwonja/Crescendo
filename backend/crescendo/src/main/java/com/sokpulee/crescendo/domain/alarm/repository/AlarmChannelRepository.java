package com.sokpulee.crescendo.domain.alarm.repository;

import com.sokpulee.crescendo.domain.alarm.entity.AlarmChannel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmChannelRepository extends JpaRepository<AlarmChannel, Long> {
}
