package com.sokpulee.crescendo.domain.alarm.service;

import com.sokpulee.crescendo.domain.alarm.dto.response.GetAlarmResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface AlarmService {
    SseEmitter connect(Long userId);

    Page<GetAlarmResponse> getAlarms(Long loggedInUserId, Pageable pageable);

    long countUnreadAlarmsByUserId(Long loggedInUserId);

    void readAlarm(Long loggedInUserId, Long alarmId);

    void deleteAlarm(Long loggedInUserId, Long alarmId);

    void followAlarm(Long followingUserId, Long followerUserId, Long relatedId);

    void challengeJoinAlarm(String challengeName, Long challengeOrganizerId, Long challengeJoinerId, Long relatedId);

    void challengeJoinLikeAlarm(String challengeName, Long challengeJoinerId, Long challengeJoinLikedUserId, Long relatedId);

    void goodsLikeAlarm(String goodsTitle, Long goodsWriterId, Long goodsLikedUserId, Long relatedId);

    void goodsCommentAlarm(String goodsTitle, String comment, Long goodsWriterId, Long goodsCommenterId, Long relatedId);

    void goodsReplyAlarm(String comment, String reply, Long goodsCommenterId, Long goodsReplierId, Long relatedId);

    void goodsCommentLikeAlarm(String comment, Long goodsCommenterId, Long goodsCommentLikeUserId, Long relatedId);

    void feedLikeAlarm(String feedTitle, Long feedWriterId, Long feedLikedUserId, Long relatedId);

    void feedCommentAlarm(String idolGroupName, String comment, Long feedWriterId, Long feedCommenterId, Long relatedId);

    void feedReplyAlarm(String comment, String reply, Long feedCommenterId, Long feedReplierId, Long relatedId);

    void feedCommentLikeAlarm(String comment, Long feedCommenterId, Long feedCommentLikeUserId, Long relatedId);

    void fanArtLikeAlarm(String fanArtTitle, Long fanArtWriterId, Long fanArtLikedUserId, Long relatedId);

    void fanArtCommentAlarm(String fanArtTitle, String comment, Long fanArtWriterId, Long fanArtCommenterId, Long relatedId);
}
