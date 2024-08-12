package com.sokpulee.crescendo.domain.alarm.repository.alarm;

import com.sokpulee.crescendo.domain.alarm.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long>, AlarmRepositoryCustom {
    long countByUserIdAndIsReadFalse(Long loggedInUserId);
}
