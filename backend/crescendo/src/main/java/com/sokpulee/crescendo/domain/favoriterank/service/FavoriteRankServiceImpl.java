package com.sokpulee.crescendo.domain.favoriterank.service;

import com.sokpulee.crescendo.domain.favoriterank.dto.request.FavoriteRankAddRequest;
import com.sokpulee.crescendo.domain.favoriterank.dto.request.FavoriteRanksSearchCondition;
import com.sokpulee.crescendo.domain.favoriterank.dto.response.FavoriteRankResponse;
import com.sokpulee.crescendo.domain.favoriterank.entity.FavoriteRank;
import com.sokpulee.crescendo.domain.favoriterank.repository.FavoriteRankRepository;
import com.sokpulee.crescendo.domain.idol.entity.Idol;
import com.sokpulee.crescendo.domain.idol.repository.IdolRepository;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.domain.user.repository.UserRepository;
import com.sokpulee.crescendo.global.exception.custom.DuplicateFavoriteRankException;
import com.sokpulee.crescendo.global.exception.custom.IdolNotFoundException;
import com.sokpulee.crescendo.global.exception.custom.UserNotFoundException;
import com.sokpulee.crescendo.global.util.file.FileSaveHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
@RequiredArgsConstructor
public class FavoriteRankServiceImpl implements FavoriteRankService {

    private final FavoriteRankRepository favoriteRankRepository;
    private final UserRepository userRepository;
    private final IdolRepository idolRepository;
    private final FileSaveHelper fileSaveHelper;

    @Override
    public void updateIdolProfileWithTopFavoriteImage() {
        LocalDateTime startDate = LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endDate = LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        List<Object[]> favoriteImagesWithVoteCount = favoriteRankRepository.findFavoriteImagesWithVoteCountForMonth(startDate, endDate);

        Map<Idol, String> idolTopFavoriteImageMap = new HashMap<>();
        for (Object[] result : favoriteImagesWithVoteCount) {
            Idol idol = (Idol) result[0];
            String favoriteImagePath = (String) result[1];
            Long voteCount = (Long) result[2];

            // If this idol is not yet in the map, add it
            if (!idolTopFavoriteImageMap.containsKey(idol)) {
                idolTopFavoriteImageMap.put(idol, favoriteImagePath);
            }
        }

        for (Map.Entry<Idol, String> entry : idolTopFavoriteImageMap.entrySet()) {
            Idol idol = entry.getKey();
            String favoriteImagePath = entry.getValue();

            idol.updateProfile2(favoriteImagePath);
        }
    }

    @Override
    public void registerFavoriteRank(Long loggedInUserId, FavoriteRankAddRequest favoriteRankAddRequest) {

        // 현재 날짜를 기준으로 해당 달의 시작과 끝 날짜 계산
        LocalDateTime startDate = LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endDate = LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        Idol idol = idolRepository.findById(favoriteRankAddRequest.getIdolId())
                .orElseThrow(IdolNotFoundException::new);

        boolean exists = favoriteRankRepository.existsByUserAndCreatedAtBetween(user, startDate, endDate);
        if (exists) {
            throw new DuplicateFavoriteRankException();
        }

        String saveImagePath = fileSaveHelper.saveFavoriteRankImage(favoriteRankAddRequest.getFavoriteIdolImage());

        FavoriteRank favoriteRank = FavoriteRank
                .builder()
                .user(user)
                .idol(idol)
                .favoriteIdolImagePath(saveImagePath)
                .build();

        favoriteRankRepository.save(favoriteRank);
    }

    @Override
    public Page<FavoriteRankResponse> getFavoriteRanks(Long userId, FavoriteRanksSearchCondition condition, Pageable pageable) {
        return favoriteRankRepository.findFavoriteRank(userId, condition, pageable);
    }
}
