package com.sokpulee.crescendo.global.scheduler;

import com.sokpulee.crescendo.domain.favoriterank.service.FavoriteRankService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class FavoriteRankScheduledTasks {

    private final FavoriteRankService favoriteRankService;

    // 매달 첫 번째 날 자정에 실행
    @Scheduled(cron = "0 0 0 1 * ?")
    public void updateMonthlyIdolProfiles() {
        favoriteRankService.updateIdolProfileWithTopFavoriteImage();
    }
}
