package com.sokpulee.crescendo.domain.idol.service;

import com.sokpulee.crescendo.domain.idol.dto.request.IdolNameListResponse;

public interface IdolService {

    IdolNameListResponse getIdolNameListByGroupId(Long idolGroupId);
}
