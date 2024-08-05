package com.sokpulee.crescendo.domain.idol.service;

import com.sokpulee.crescendo.domain.idol.dto.request.IdealWorldCupFinishRequest;
import com.sokpulee.crescendo.domain.idol.dto.request.IdolNameListResponse;
import com.sokpulee.crescendo.domain.idol.dto.request.StartIdealWorldCupRequest;
import com.sokpulee.crescendo.domain.idol.dto.response.IdealWorldCupStartResponse;

public interface IdolService {

    IdolNameListResponse getIdolNameListByGroupId(Long idolGroupId);

    IdealWorldCupStartResponse getRandomIdols(StartIdealWorldCupRequest startIdealWorldCupRequest);

    void plusIdealWorldCupWinNum(IdealWorldCupFinishRequest idealWorldCupFinishRequest);

}
