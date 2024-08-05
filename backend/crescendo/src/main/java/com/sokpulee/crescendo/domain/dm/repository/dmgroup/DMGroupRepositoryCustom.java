package com.sokpulee.crescendo.domain.dm.repository.dmgroup;

import com.sokpulee.crescendo.domain.dm.dto.response.MyDmGroupResponseDto;

import java.util.List;

public interface DMGroupRepositoryCustom {
    boolean existsByUserIdAndDmGroupId(Long loggedInUserId, Long dmGroupId);

    List<MyDmGroupResponseDto> findDmGroupsWithLastMessage(Long userId);
}
