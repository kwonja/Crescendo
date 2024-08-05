package com.sokpulee.crescendo.domain.dm.service;

import com.sokpulee.crescendo.domain.dm.dto.request.DmGroupCreateRequest;
import com.sokpulee.crescendo.domain.dm.dto.request.MessageRequest;
import com.sokpulee.crescendo.domain.dm.dto.response.*;
import com.sokpulee.crescendo.domain.dm.entity.DmGroup;
import com.sokpulee.crescendo.domain.dm.entity.DmMessage;
import com.sokpulee.crescendo.domain.dm.entity.DmParticipants;
import com.sokpulee.crescendo.domain.dm.repository.dmgroup.DMGroupRepository;
import com.sokpulee.crescendo.domain.dm.repository.dmmessage.DMMessageRepository;
import com.sokpulee.crescendo.domain.dm.repository.DMParticipantsRepository;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.domain.user.repository.UserRepository;
import com.sokpulee.crescendo.global.exception.custom.DMGroupNotFoundException;
import com.sokpulee.crescendo.global.exception.custom.UnAuthorizedAccessException;
import com.sokpulee.crescendo.global.exception.custom.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

        DmGroup dmGroup = new DmGroup();

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

    @Override
    public MyDMGroupIdListResponse findAllDmGroupsByUserId(Long loggedInUserId) {

        List<DmGroup> dmGroupList = dmGroupRepository.findAllByUserId(loggedInUserId);

        return new MyDMGroupIdListResponse(dmGroupList);
    }

    @Override
    public List<MyDmGroupResponseDto> findDmGroupsByUserId(Long loggedInUserId) {

        return dmGroupRepository.findDmGroupsWithLastMessage(loggedInUserId);
    }

    @Override
    public MessageResponse saveMessage(MessageRequest message) {

        DmGroup dmGroup = dmGroupRepository.findById(message.getDmGroupId())
                .orElseThrow(DMGroupNotFoundException::new);

        User user = userRepository.findById(message.getWriterId())
                .orElseThrow(UserNotFoundException::new);

        DmMessage dmMessage = DmMessage.builder()
                .dmGroup(dmGroup)
                .user(user)
                .content(message.getMessage())
                .build();

        DmMessage saveDMMessage = dmMessageRepository.save(dmMessage);


        return new MessageResponse(saveDMMessage, user);
    }

    @Override
    public Page<DmMessageResponseDto> findMessagesByDmGroupId(Long loggedInUserId, Long dmGroupId, Pageable pageable) {

        if (!dmGroupRepository.existsByUserIdAndDmGroupId(loggedInUserId, dmGroupId)) {
            throw new UnAuthorizedAccessException();
        }

        return dmMessageRepository.findMessagesByDmGroupId(dmGroupId, pageable);
    }
}
