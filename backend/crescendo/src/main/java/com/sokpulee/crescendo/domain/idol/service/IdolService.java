package com.sokpulee.crescendo.domain.idol.service;

import com.sokpulee.crescendo.domain.idol.dto.request.IdealWorldCupFinishRequest;
import com.sokpulee.crescendo.domain.idol.dto.request.IdolNameListResponse;
import com.sokpulee.crescendo.domain.idol.dto.request.StartIdealWorldCupRequest;
import com.sokpulee.crescendo.domain.idol.dto.response.IdealWorldCupRankResponse;
import com.sokpulee.crescendo.domain.idol.dto.response.IdealWorldCupStartResponse;
import com.sokpulee.crescendo.domain.idol.enums.Gender;
import org.springframework.data.domain.Page;

public interface IdolService {

    IdolNameListResponse getIdolNameListByGroupId(Long idolGroupId);

    IdealWorldCupStartResponse getRandomIdols(StartIdealWorldCupRequest startIdealWorldCupRequest);

    void plusIdealWorldCupWinNum(IdealWorldCupFinishRequest idealWorldCupFinishRequest);

    Page<IdealWorldCupRankResponse> getIdealWorldCupRank(int page, int size, String idolName, Gender gender);
}
