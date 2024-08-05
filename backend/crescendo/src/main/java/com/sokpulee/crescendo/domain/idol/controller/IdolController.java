package com.sokpulee.crescendo.domain.idol.controller;

import com.sokpulee.crescendo.domain.idol.dto.request.IdealWorldCupFinishRequest;
import com.sokpulee.crescendo.domain.idol.dto.response.IdealWorldCupStartResponse;
import com.sokpulee.crescendo.domain.idol.service.IdolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/idol")
@Tag(name = "Idol", description = "아이돌 관련 API")
public class IdolController {

    private final IdolService idolService;

    @GetMapping("/idol-group/{idol-group-id}/name")
    @Operation(summary = "아이돌 조회(이름)", description = "아이돌 조회(이름) API")
    public ResponseEntity<?> getIdolNameList(@PathVariable("idol-group-id") Long idolGroupId) {

        return ResponseEntity.status(OK).body(idolService.getIdolNameListByGroupId(idolGroupId));
    }

    @GetMapping("/ideal-world-cup")
    @Operation(summary = "이상형 월드컵 시작", description = "이상형 월드컵 시작 API")
    public ResponseEntity<?> startIdealWorldCup(@RequestParam(value = "num", required = true) Integer num) {

        IdealWorldCupStartResponse randomIdols = idolService.getRandomIdols(num);

        return ResponseEntity.status(OK).body(randomIdols);
    }

    @PostMapping("/ideal-world-cup/finish")
    @Operation(summary = "이상형 월드컵 종료", description = "이상형 월드컵 종료 API")
    public ResponseEntity<?> finishIdealWorldCup(@Valid @RequestBody IdealWorldCupFinishRequest idealWorldCupFinishRequest) {

        idolService.plusIdealWorldCupWinNum(idealWorldCupFinishRequest);

        return ResponseEntity.status(NO_CONTENT).build();
    }

}
