package com.sokpulee.crescendo.domain.dm.controller;

import com.sokpulee.crescendo.domain.dm.dto.request.DmGroupCreateRequest;
import com.sokpulee.crescendo.domain.dm.dto.response.DMGroupCreateResponse;
import com.sokpulee.crescendo.domain.dm.dto.response.DMGroupGetResponse;
import com.sokpulee.crescendo.domain.dm.service.DMService;
import com.sokpulee.crescendo.global.auth.annotation.AuthPrincipal;
import com.sokpulee.crescendo.global.exception.custom.AuthenticationRequiredException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dm")
@Tag(name = "DM", description = "DM 관련 API")
public class DMController {

    private final DMService dmService;

    @PostMapping("/dm-group")
    @Operation(summary = "DM 그룹 생성", description = "DM 그룹 생성 API")
    public ResponseEntity<?> createDMGroup(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @RequestBody DmGroupCreateRequest dmGroupCreateRequest
    ) {
        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        DMGroupCreateResponse response = dmService.createDMGroup(loggedInUserId, dmGroupCreateRequest);

        return ResponseEntity.status(CREATED).body(response);
    }

    @GetMapping("/user/{opponent-id}")
    @Operation(summary = "DM 그룹 조회", description = "DM 그룹 조회 API")
    public ResponseEntity<?> getDMGroup(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("opponent-id") Long opponentId
    ) {
        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        DMGroupGetResponse dmGroup = dmService.findDmGroup(loggedInUserId, opponentId);

        return ResponseEntity.status(OK).body(dmGroup);
    }
}
