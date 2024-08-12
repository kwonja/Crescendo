package com.sokpulee.crescendo.domain.alarm.repository.alarm;

import com.sokpulee.crescendo.domain.alarm.dto.response.GetAlarmResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlarmRepositoryCustom {

    Page<GetAlarmResponse> findAllByUserId(Long userId, Pageable pageable);
}
