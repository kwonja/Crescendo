package com.sokpulee.crescendo.domain.dm.service;

import com.sokpulee.crescendo.domain.dm.dto.request.DmGroupCreateRequest;
import com.sokpulee.crescendo.domain.dm.dto.response.DMGroupCreateResponse;
import com.sokpulee.crescendo.domain.dm.dto.response.DMGroupGetResponse;
import com.sokpulee.crescendo.domain.dm.entity.DmGroup;
import com.sokpulee.crescendo.domain.dm.entity.DmParticipants;
import com.sokpulee.crescendo.domain.dm.repository.DMGroupRepository;
import com.sokpulee.crescendo.domain.dm.repository.DMMessageRepository;
import com.sokpulee.crescendo.domain.dm.repository.DMParticipantsRepository;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.domain.user.repository.UserRepository;
import com.sokpulee.crescendo.global.exception.custom.DMGroupNotFoundException;
import com.sokpulee.crescendo.global.exception.custom.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class DMServiceImpl implements DMService {

    private final DMGroupRepository dmGroupRepository;
    private final DMMessageRepository dmMessageRepository;
    private final DMParticipantsRepository dmParticipantsRepository;
    private final UserRepository userRepository;

    @Override
    public DMGroupCreateResponse createDMGroup(Long loggedInUserId, DmGroupCreateRequest dmGroupCreateRequest) {

        User loggedInUser = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        User opponentUser = userRepository.findById(dmGroupCreateRequest.getOpponentId())
                .orElseThrow(UserNotFoundException::new);

        Optional<DmGroup> existingGroup = dmGroupRepository.findDmGroupByParticipants(loggedInUserId, dmGroupCreateRequest.getOpponentId());
        if (existingGroup.isPresent()) {
            throw new IllegalStateException("DM group already exists between the users");
        }

        DmGroup dmGroup = DmGroup.builder().build();

        DmParticipants loggedInParticipants = DmParticipants
                .builder()
                .user(loggedInUser)
                .build();

        DmParticipants opponentParticipants = DmParticipants
                .builder()
                .user(opponentUser)
                .build();

        dmGroup.addDmParticipant(loggedInParticipants);
        dmGroup.addDmParticipant(opponentParticipants);

        DmGroup saveDmGroup = dmGroupRepository.save(dmGroup);

        return new DMGroupCreateResponse(saveDmGroup);
    }


    @Override
    public DMGroupGetResponse findDmGroup(Long loggedInUserId, Long opponentId) {

        userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        userRepository.findById(opponentId)
                .orElseThrow(UserNotFoundException::new);

        DmGroup dmGroup = dmGroupRepository.findDmGroupByParticipants(loggedInUserId, opponentId)
                .orElseThrow(DMGroupNotFoundException::new);

        return new DMGroupGetResponse(dmGroup);
    }

    @Override
    public void deleteDmGroup(Long loggedInUserId, Long opponentId) {

        DmGroup dmGroup = dmGroupRepository.findDmGroupByParticipants(loggedInUserId, opponentId)
                .orElseThrow(DMGroupNotFoundException::new);

        dmGroupRepository.delete(dmGroup);
    }
}
