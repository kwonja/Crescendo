package com.sokpulee.crescendo.domain.dm.controller;

import com.sokpulee.crescendo.domain.dm.dto.request.DmGroupCreateRequest;
import com.sokpulee.crescendo.domain.dm.dto.response.DMGroupCreateResponse;
import com.sokpulee.crescendo.domain.dm.dto.response.DMGroupGetResponse;
import com.sokpulee.crescendo.domain.dm.dto.response.DmGroupResponseDto;
import com.sokpulee.crescendo.domain.dm.dto.response.MyDMGroupIdListResponse;
import com.sokpulee.crescendo.domain.dm.service.DMService;
import com.sokpulee.crescendo.global.auth.annotation.AuthPrincipal;
import com.sokpulee.crescendo.global.exception.custom.AuthenticationRequiredException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    @Operation(summary = "DM 그룹 생성(상대방과 DM 그룹이 없을 때 생성)", description = "DM 그룹 생성 API")
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

    @GetMapping("/dm-group/user/{opponent-id}")
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

    @DeleteMapping("/dm-group/user/{opponent-id}")
    @Operation(summary = "DM 그룹 삭제", description = "DM 그룹 삭제 API")
    public ResponseEntity<?> deleteDMGroup(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @PathVariable("opponent-id") Long opponentId
    ) {

        dmService.deleteDmGroup(loggedInUserId, opponentId);

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @GetMapping("/my-dm-group/simple")
    @Operation(summary = "내 DM 그룹 목록 조회(구독용)", description = "내 DM 그룹 목록 조회(구독용) API")
    public ResponseEntity<?> getMyDMGroupSimple(@Parameter(hidden = true) @AuthPrincipal Long loggedInUserId) {

        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }

        MyDMGroupIdListResponse response =  dmService.findAllDmGroupsByUserId(loggedInUserId);
        return ResponseEntity.status(OK).body(response);
    }

    @GetMapping("/my-dm-group")
    @Operation(summary = "내 DM 목록 조회(내 대화 리스트 조회용)", description = "내 DM 목록 조회(내 대화 리스트 조회용) API")
    public ResponseEntity<?> getMyDmGroups(
            @Parameter(hidden = true) @AuthPrincipal Long loggedInUserId,
            @RequestParam int page,
            @RequestParam int size
    ) {
        if(loggedInUserId == null) {
            throw new AuthenticationRequiredException();
        }
        
        Pageable pageable = PageRequest.of(page, size);
        Page<DmGroupResponseDto> dmGroups = dmService.findDmGroupsByUserId(loggedInUserId, pageable);
        return ResponseEntity.ok(dmGroups);
    }
}
