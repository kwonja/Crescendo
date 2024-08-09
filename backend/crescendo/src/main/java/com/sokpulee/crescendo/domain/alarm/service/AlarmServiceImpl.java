package com.sokpulee.crescendo.domain.alarm.service;

import com.sokpulee.crescendo.domain.alarm.dto.AlarmDto;
import com.sokpulee.crescendo.domain.alarm.dto.AlarmType;
import com.sokpulee.crescendo.domain.alarm.dto.response.GetAlarmResponse;
import com.sokpulee.crescendo.domain.alarm.entity.Alarm;
import com.sokpulee.crescendo.domain.alarm.entity.AlarmChannel;
import com.sokpulee.crescendo.domain.alarm.repository.AlarmChannelRepository;
import com.sokpulee.crescendo.domain.alarm.repository.alarm.AlarmRepository;
import com.sokpulee.crescendo.domain.alarm.repository.EmitterRepository;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.domain.user.repository.UserRepository;
import com.sokpulee.crescendo.global.exception.custom.AlarmChannelNotFoundException;
import com.sokpulee.crescendo.global.exception.custom.AlarmNotFoundException;
import com.sokpulee.crescendo.global.exception.custom.UnAuthorizedAccessException;
import com.sokpulee.crescendo.global.exception.custom.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Transactional
@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    // 기본 타임아웃 설정
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;
    private final AlarmRepository alarmRepository;
    private final AlarmChannelRepository alarmChannelRepository;
    private final UserRepository userRepository;

    @Override
    public SseEmitter connect(Long userId) {
        SseEmitter emitter = createEmitter(userId);

        sendToClient(userId, "Connected! [userId=" + userId + "]");
        return emitter;
    }

    @Override
    public Page<GetAlarmResponse> getAlarms(Long loggedInUserId, Pageable pageable) {
        return alarmRepository.findAllByUserId(loggedInUserId, pageable);
    }

    @Override
    public long countUnreadAlarmsByUserId(Long loggedInUserId) {
        return alarmRepository.countByUserIdAndIsReadFalse(loggedInUserId);
    }

    @Override
    public void readAlarm(Long loggedInUserId, Long alarmId) {

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(AlarmNotFoundException::new);

        if(!alarm.getUser().getId().equals(user.getId())) {
            throw new UnAuthorizedAccessException();
        }

        alarm.readAlarm();
    }

    @Override
    public void deleteAlarm(Long loggedInUserId, Long alarmId) {

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(AlarmNotFoundException::new);

        if(!alarm.getUser().getId().equals(user.getId())) {
            throw new UnAuthorizedAccessException();
        }

        alarmRepository.delete(alarm);
    }

    private void sendToClient(Long id, Object data) {
        SseEmitter emitter = emitterRepository.get(id);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("connect").data(data));
            } catch (IOException exception) {
                emitterRepository.deleteById(id);
                emitter.completeWithError(exception);
            }
        }
    }

    private SseEmitter createEmitter(Long userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, emitter);

        emitter.onCompletion(() -> emitterRepository.deleteById(userId));
        emitter.onTimeout(() -> emitterRepository.deleteById(userId));

        return emitter;
    }

    @Override
    public void followAlarm(Long followingUserId, Long followerUserId, Long relatedId) {

        User user = userRepository.findById(followingUserId)
                .orElseThrow(UserNotFoundException::new);

        String content = user.getNickname() + "님께서 회원님을 팔로우 하셨습니다.";

        sendToAlarm(new AlarmDto(followerUserId, AlarmType.FOLLOW.getId(), relatedId, content));
    }

    @Override
    public void challengeJoinAlarm(String challengeName, Long challengeOrganizerId, Long challengeJoinerId, Long relatedId) {

        User joiner = userRepository.findById(challengeJoinerId)
                .orElseThrow(UserNotFoundException::new);

        String content = joiner.getNickname() + "님께서 " + challengeName + " 챌린지에 참여하셨습니다.";

        sendToAlarm(new AlarmDto(challengeOrganizerId, AlarmType.CHALLENGE.getId(), relatedId, content));

    }

    @Override
    public void challengeJoinLikeAlarm(String challengeName, Long challengeJoinerId, Long challengeJoinLikedUserId, Long relatedId) {

        User likedUser = userRepository.findById(challengeJoinLikedUserId)
                .orElseThrow(UserNotFoundException::new);

        String content = likedUser.getNickname() + "님께서 " + challengeName + " 챌린지에 좋아요를 누르셨습니다.";

        sendToAlarm(new AlarmDto(challengeJoinerId, AlarmType.CHALLENGE.getId(), relatedId, content));
    }

    @Override
    public void goodsLikeAlarm(String goodsTitle, Long goodsWriterId, Long goodsLikedUserId, Long relatedId) {

        User likedUser = userRepository.findById(goodsLikedUserId)
                .orElseThrow(UserNotFoundException::new);

        String content = likedUser.getNickname() + "님께서 " + goodsTitle + " 에 좋아요를 누르셨습니다.";

        sendToAlarm(new AlarmDto(goodsWriterId, AlarmType.GOODS.getId(), relatedId, content));
    }

    @Override
    public void goodsCommentAlarm(String goodsTitle, String comment, Long goodsWriterId, Long goodsCommenterId, Long relatedId) {

        User commenter = userRepository.findById(goodsCommenterId)
                .orElseThrow(UserNotFoundException::new);

        String content = commenter.getNickname() + "님께서 " + goodsTitle + "에 댓글을 추가하셨습니다. " + "\"" +  comment + "\"";

        sendToAlarm(new AlarmDto(goodsWriterId, AlarmType.GOODS.getId(), relatedId, content));
    }

    @Override
    public void goodsReplyAlarm(String comment, String reply, Long goodsCommenterId, Long goodsReplierId, Long relatedId) {

        User replier = userRepository.findById(goodsReplierId)
                .orElseThrow(UserNotFoundException::new);

        String content = replier.getNickname() + "님께서 " + "\"" + comment + "\"" + " 댓글에 대댓글을 추가하셨습니다. " + "\"" +  reply+ "\"";

        sendToAlarm(new AlarmDto(goodsCommenterId, AlarmType.GOODS.getId(), relatedId, content));
    }

    @Override
    public void goodsCommentLikeAlarm(String comment, Long goodsCommenterId, Long goodsCommentLikeUserId, Long relatedId) {

        User likedUser = userRepository.findById(goodsCommentLikeUserId)
                .orElseThrow(UserNotFoundException::new);

        String content = likedUser.getNickname() + "님께서 " + "\"" + comment + "\"" + " 댓글에 좋아요를 누르셨습니다.";

        sendToAlarm(new AlarmDto(goodsCommenterId, AlarmType.GOODS.getId(), relatedId, content));
    }

    @Override
    public void feedLikeAlarm(String idolGroupName, Long feedWriterId, Long feedLikedUserId, Long relatedId) {

        User likedUser = userRepository.findById(feedLikedUserId)
                .orElseThrow(UserNotFoundException::new);

        String content = likedUser.getNickname() + "님께서 " + idolGroupName + " 커뮤니티에 올리신 피드에 좋아요를 누르셨습니다.";

        sendToAlarm(new AlarmDto(feedWriterId, AlarmType.FEED.getId(), relatedId, content));
    }

    @Override
    public void feedCommentAlarm(String idolGroupName, String comment, Long feedWriterId, Long feedCommenterId, Long relatedId) {

        User commenter = userRepository.findById(feedCommenterId)
                .orElseThrow(UserNotFoundException::new);

        String content = commenter.getNickname() + "님께서 " + idolGroupName + " 커뮤니티에 올리신 피드에 댓글을 추가하셨습니다. " + "\"" +  comment + "\"";

        sendToAlarm(new AlarmDto(feedWriterId, AlarmType.FEED.getId(), relatedId, content));
    }

    @Override
    public void feedReplyAlarm(String comment, String reply, Long feedCommenterId, Long feedReplierId, Long relatedId) {

        User replier = userRepository.findById(feedReplierId)
                .orElseThrow(UserNotFoundException::new);

        String content = replier.getNickname() + "님께서 " + "\"" + comment + "\"" + " 댓글에 대댓글을 추가하셨습니다. " + "\"" +  reply+ "\"";

        sendToAlarm(new AlarmDto(feedCommenterId, AlarmType.FEED.getId(), relatedId, content));
    }

    @Override
    public void feedCommentLikeAlarm(String comment, Long feedCommenterId, Long feedCommentLikeUserId, Long relatedId) {

        User likedUser = userRepository.findById(feedCommentLikeUserId)
                .orElseThrow(UserNotFoundException::new);

        String content = likedUser.getNickname() + "님께서 " + "\"" + comment + "\"" + " 댓글에 좋아요를 누르셨습니다.";

        sendToAlarm(new AlarmDto(feedCommenterId, AlarmType.FEED.getId(), relatedId, content));
    }

    @Override
    public void fanArtLikeAlarm(String fanArtTitle, Long fanArtWriterId, Long fanArtLikedUserId, Long relatedId) {

        User likedUser = userRepository.findById(fanArtLikedUserId)
                .orElseThrow(UserNotFoundException::new);

        String content = likedUser.getNickname() + "님께서 " + fanArtTitle + "에 좋아요를 누르셨습니다.";

        sendToAlarm(new AlarmDto(fanArtWriterId, AlarmType.FANART.getId(), relatedId, content));
    }

    @Override
    public void fanArtCommentAlarm(String fanArtTitle, String comment, Long fanArtWriterId, Long fanArtCommenterId, Long relatedId) {

        User commenter = userRepository.findById(fanArtCommenterId)
                .orElseThrow(UserNotFoundException::new);

        String content = commenter.getNickname() + "님께서 " + fanArtTitle + "에 댓글을 추가하셨습니다. " + "\"" +  comment + "\"";

        sendToAlarm(new AlarmDto(fanArtWriterId, AlarmType.FANART.getId(), relatedId, content));
    }

    @Override
    public void fanArtReplyAlarm(String comment, String reply, Long fanArtCommenterId, Long fanArtReplierId, Long relatedId) {

        User replier = userRepository.findById(fanArtReplierId)
                .orElseThrow(UserNotFoundException::new);

        String content = replier.getNickname() + "님께서 " + "\"" + comment + "\"" + " 댓글에 대댓글을 추가하셨습니다. " + "\"" +  reply+ "\"";

        sendToAlarm(new AlarmDto(fanArtCommenterId, AlarmType.FANART.getId(), relatedId, content));
    }


    public void sendToAlarm(AlarmDto alarmDto) {

        User user = userRepository.findById(alarmDto.getUserId())
                .orElseThrow(UserNotFoundException::new);

        AlarmChannel alarmChannel = alarmChannelRepository.findById(alarmDto.getAlarmChannelId())
                .orElseThrow(AlarmChannelNotFoundException::new);

        alarmRepository.save(alarmDto.toEntity(user, alarmChannel));

        SseEmitter emitter = emitterRepository.get(alarmDto.getUserId());
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("alarm").data(alarmDto.getContent()));
            } catch (IOException exception) {
                emitterRepository.deleteById(alarmDto.getUserId());
                emitter.completeWithError(exception);
            }
        }
    }
}
