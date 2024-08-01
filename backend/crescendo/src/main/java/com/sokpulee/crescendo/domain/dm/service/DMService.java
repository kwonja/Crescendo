package com.sokpulee.crescendo.domain.dm.service;

import com.sokpulee.crescendo.domain.dm.dto.request.DmGroupCreateRequest;
import com.sokpulee.crescendo.domain.dm.dto.request.MessageRequest;
import com.sokpulee.crescendo.domain.dm.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DMService {
    DMGroupCreateResponse createDMGroup(Long loggedInUserId, DmGroupCreateRequest dmGroupCreateRequest);
    DMGroupGetResponse findDmGroup(Long loggedInUserId, Long opponentId);

    void deleteDmGroup(Long loggedInUserId, Long opponentId);

    MyDMGroupIdListResponse findAllDmGroupsByUserId(Long loggedInUserId);

    Page<DmGroupResponseDto> findDmGroupsByUserId(Long loggedInUserId, Pageable pageable);

    MessageResponse saveMessage(MessageRequest message);
}
