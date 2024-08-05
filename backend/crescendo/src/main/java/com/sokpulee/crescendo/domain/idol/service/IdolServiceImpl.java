package com.sokpulee.crescendo.domain.idol.service;

import com.sokpulee.crescendo.domain.idol.dto.IdolInfoDto;
import com.sokpulee.crescendo.domain.idol.dto.IdolNameDto;
import com.sokpulee.crescendo.domain.idol.dto.request.IdealWorldCupFinishRequest;
import com.sokpulee.crescendo.domain.idol.dto.request.IdolNameListResponse;
import com.sokpulee.crescendo.domain.idol.dto.response.IdealWorldCupStartResponse;
import com.sokpulee.crescendo.domain.idol.entity.Idol;
import com.sokpulee.crescendo.domain.idol.entity.IdolGroup;
import com.sokpulee.crescendo.domain.idol.repository.IdolGroupRepository;
import com.sokpulee.crescendo.domain.idol.repository.IdolRepository;
import com.sokpulee.crescendo.global.exception.custom.IdolNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class IdolServiceImpl implements IdolService {

    private final IdolRepository idolRepository;
    private final IdolGroupRepository idolGroupRepository;

    @Override
    public IdolNameListResponse getIdolNameListByGroupId(Long idolGroupId) {

        IdolGroup idolGroup = idolGroupRepository.findById(idolGroupId)
                .orElseThrow(IdolNotFoundException::new);

        List<IdolNameDto> idolList = idolRepository.findByIdolGroup(idolGroup).stream()
                .map(idol -> new IdolNameDto(idol.getId(), idol.getName()))
                .toList();

        return new IdolNameListResponse(idolList);
    }

    @Override
    public IdealWorldCupStartResponse getRandomIdols(int num) {
        List<Idol> allIdols = idolRepository.findAllWithGroup();

        Collections.shuffle(allIdols);

        List<Idol> randomIdols = allIdols.stream()
                .limit(num)
                .toList();

        List<IdolInfoDto> idolList = randomIdols.stream()
                .map(idol -> new IdolInfoDto(
                        idol.getId(),
                        idol.getIdolGroup().getName(),
                        idol.getName(),
                        idol.getProfile()
                ))
                .toList();


        return new IdealWorldCupStartResponse(idolList);
    }

    @Override
    public void plusIdealWorldCupWinNum(IdealWorldCupFinishRequest idealWorldCupFinishRequest) {
        Idol idol = idolRepository.findById(idealWorldCupFinishRequest.getIdolId())
                .orElseThrow(IdolNotFoundException::new);

        idol.plusWinNum();
    }
}
