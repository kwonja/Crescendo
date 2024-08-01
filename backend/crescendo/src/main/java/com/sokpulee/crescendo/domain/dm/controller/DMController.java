package com.sokpulee.crescendo.domain.dm.controller;

import com.sokpulee.crescendo.domain.dm.dto.request.DmGroupCreateRequest;
import com.sokpulee.crescendo.domain.dm.dto.response.DMGroupCreateResponse;
import com.sokpulee.crescendo.domain.dm.service.DMService;
import com.sokpulee.crescendo.global.auth.annotation.AuthPrincipal;
import com.sokpulee.crescendo.global.exception.custom.AuthenticationRequiredException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dm")
@Tag(name = "DM", description = "DM 관련 API")
public class DMController {

    private final DMService dmService;

    @PostMapping("/dm-group")
    @Operation(summary = "dm 그룹 생성", description = "회원가입 API")
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
}
