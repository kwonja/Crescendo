package com.sokpulee.crescendo.domain.idol.service;

import com.sokpulee.crescendo.domain.idol.dto.IdolInfoDto;
import com.sokpulee.crescendo.domain.idol.dto.IdolNameDto;
import com.sokpulee.crescendo.domain.idol.dto.request.IdealWorldCupFinishRequest;
import com.sokpulee.crescendo.domain.idol.dto.request.IdolNameListResponse;
import com.sokpulee.crescendo.domain.idol.dto.request.StartIdealWorldCupRequest;
import com.sokpulee.crescendo.domain.idol.dto.response.IdealWorldCupRankResponse;
import com.sokpulee.crescendo.domain.idol.dto.response.IdealWorldCupStartResponse;
import com.sokpulee.crescendo.domain.idol.entity.Idol;
import com.sokpulee.crescendo.domain.idol.entity.IdolGroup;
import com.sokpulee.crescendo.domain.idol.enums.Gender;
import com.sokpulee.crescendo.domain.idol.repository.IdolGroupRepository;
import com.sokpulee.crescendo.domain.idol.repository.idol.IdolRepository;
import com.sokpulee.crescendo.global.exception.custom.IdolNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    public IdealWorldCupStartResponse getRandomIdols(StartIdealWorldCupRequest startIdealWorldCupRequest) {
        List<Idol> allIdols = idolRepository.findAllWithGroup(startIdealWorldCupRequest.getGender());

        Collections.shuffle(allIdols);

        List<Idol> randomIdols = allIdols.stream()
                .limit(startIdealWorldCupRequest.getNum())
                .toList();

        List<IdolInfoDto> idolList = randomIdols.stream()
                .map(idol -> new IdolInfoDto(
                        idol.getId(),
                        idol.getIdolGroup().getName(),
                        idol.getName(),
                        idol.getProfile2()
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

    @Override
    public Page<IdealWorldCupRankResponse> getIdealWorldCupRank(int page, int size, String idolName, Gender gender) {

        PageRequest pageRequest = PageRequest.of(page, size);
        List<Idol> allIdols = idolRepository.findAllIdolsByGender(gender);

        // 전체 아이돌 리스트에서 순위를 매깁니다.
        List<IdealWorldCupRankResponse> rankedIdols = IntStream.range(0, allIdols.size())
                .mapToObj(i -> new IdealWorldCupRankResponse(
                        i + 1, // 순위
                        allIdols.get(i).getId(),
                        allIdols.get(i).getIdolGroup().getName(),
                        allIdols.get(i).getName(),
                        allIdols.get(i).getProfile2(),
                        allIdols.get(i).getWinNum()
                ))
                .collect(Collectors.toList());

        // 이름으로 필터링합니다.
        if (idolName != null && !idolName.isEmpty()) {
            rankedIdols = rankedIdols.stream()
                    .filter(idol -> idol.getIdolName().contains(idolName))
                    .collect(Collectors.toList());
        }

        // 페이지네이션을 적용합니다.
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), rankedIdols.size());
        List<IdealWorldCupRankResponse> pagedRankedIdols = rankedIdols.subList(start, end);

        return new PageImpl<>(pagedRankedIdols, pageRequest, rankedIdols.size());
    }
}
