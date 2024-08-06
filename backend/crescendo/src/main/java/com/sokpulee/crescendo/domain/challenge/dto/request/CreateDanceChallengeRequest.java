package com.sokpulee.crescendo.domain.challenge.dto.request;

import com.sokpulee.crescendo.domain.challenge.entity.DanceChallenge;
import com.sokpulee.crescendo.domain.user.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class CreateDanceChallengeRequest {

    @NotNull(message = "제목은 필수 값 입니다.")
    private String title;

    @NotNull(message = "비디오는 필수 값 입니다.")
    private MultipartFile video;

    @NotNull(message = "종료일은 필수 값 입니다.")
    private LocalDateTime endAt;

    public CreateDanceChallengeRequest(String title, MultipartFile video, LocalDateTime endAt) {
        this.title = title;
        this.video = video;
        this.endAt = endAt;
    }

    public DanceChallenge toEntity(User user, String videoPath) {
        return DanceChallenge.builder()
                .user(user)
                .title(title)
                .videoPath(videoPath)
                .endAt(endAt)
                .build();
    }
}
