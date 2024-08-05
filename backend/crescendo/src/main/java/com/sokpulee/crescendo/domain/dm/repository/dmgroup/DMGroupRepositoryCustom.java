package com.sokpulee.crescendo.domain.dm.repository.dmgroup;

import com.sokpulee.crescendo.domain.dm.dto.response.DmGroupResponseDto;
import com.sokpulee.crescendo.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DMGroupRepositoryCustom {
    boolean existsByUserIdAndDmGroupId(Long loggedInUserId, Long dmGroupId);

    List<DmGroupResponseDto> findDmGroupsWithLastMessage(Long userId);
}
