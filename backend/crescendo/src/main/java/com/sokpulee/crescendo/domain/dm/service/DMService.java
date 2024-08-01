package com.sokpulee.crescendo.domain.dm.service;

import com.sokpulee.crescendo.domain.dm.dto.request.DmGroupCreateRequest;
import com.sokpulee.crescendo.domain.dm.dto.response.DMGroupCreateResponse;
import com.sokpulee.crescendo.domain.dm.dto.response.DMGroupGetResponse;
import com.sokpulee.crescendo.domain.dm.dto.response.DmGroupResponseDto;
import com.sokpulee.crescendo.domain.dm.dto.response.MyDMGroupIdListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DMService {
    DMGroupCreateResponse createDMGroup(Long loggedInUserId, DmGroupCreateRequest dmGroupCreateRequest);
    DMGroupGetResponse findDmGroup(Long loggedInUserId, Long opponentId);

    void deleteDmGroup(Long loggedInUserId, Long opponentId);

    MyDMGroupIdListResponse findAllDmGroupsByUserId(Long loggedInUserId);

    Page<DmGroupResponseDto> findDmGroupsByUserId(Long loggedInUserId, Pageable pageable);
}
