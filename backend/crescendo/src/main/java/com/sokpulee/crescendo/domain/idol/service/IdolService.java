package com.sokpulee.crescendo.domain.idol.service;

import com.sokpulee.crescendo.domain.idol.dto.request.IdolNameListResponse;
import com.sokpulee.crescendo.domain.idol.dto.response.IdealWorldCupStartResponse;
import com.sokpulee.crescendo.domain.idol.entity.Idol;

import java.util.List;

public interface IdolService {

    IdolNameListResponse getIdolNameListByGroupId(Long idolGroupId);

    IdealWorldCupStartResponse getRandomIdols(int num);
}
