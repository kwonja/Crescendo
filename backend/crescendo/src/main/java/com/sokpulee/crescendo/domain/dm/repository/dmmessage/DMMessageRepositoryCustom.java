package com.sokpulee.crescendo.domain.dm.repository.dmmessage;

import com.sokpulee.crescendo.domain.dm.dto.response.DmMessageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DMMessageRepositoryCustom {
    Page<DmMessageResponseDto> findMessagesByDmGroupId(Long dmGroupId, Pageable pageable);
}
