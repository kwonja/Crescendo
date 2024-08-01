package com.sokpulee.crescendo.domain.dm.service;

import com.sokpulee.crescendo.domain.dm.dto.request.DmGroupCreateRequest;
import com.sokpulee.crescendo.domain.dm.dto.response.DMGroupCreateResponse;

public interface DMService {
    DMGroupCreateResponse createDMGroup(Long loggedInUserId, DmGroupCreateRequest dmGroupCreateRequest);
}
