package com.sokpulee.crescendo.domain.idol.controller;

import com.sokpulee.crescendo.domain.idol.dto.request.IdolNameListResponse;
import com.sokpulee.crescendo.domain.idol.service.IdolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
