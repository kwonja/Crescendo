package com.sokpulee.crescendo.domain.dm.repository.dmgroup;

import com.sokpulee.crescendo.domain.dm.dto.response.DmGroupResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DMGroupRepositoryCustom {
    Page<DmGroupResponseDto> findDmGroupsByUserId(Long userId, Pageable pageable);
}
