package com.sokpulee.crescendo.domain.community.service;

import com.sokpulee.crescendo.domain.community.dto.response.FavoritesGetResponse;
import com.sokpulee.crescendo.domain.community.dto.response.IdolGroupDetailResponse;
import com.sokpulee.crescendo.domain.community.dto.response.IdolGroupGetResponse;
import com.sokpulee.crescendo.domain.community.entity.CommunityFavorites;
import com.sokpulee.crescendo.domain.community.repository.CommunityFavoritesRepository;
import com.sokpulee.crescendo.domain.idol.entity.IdolGroup;
import com.sokpulee.crescendo.domain.idol.repository.IdolGroupRepository;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.domain.user.repository.UserRepository;
import com.sokpulee.crescendo.global.exception.custom.IdolGroupNotFoundException;
import com.sokpulee.crescendo.global.exception.custom.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    private final CommunityFavoritesRepository communityFavoritesRepository;
    private final UserRepository userRepository;
    private  final IdolGroupRepository idolGroupRepository;

    @Override
    public void toggleFavorite(Long idolGroupId, Long loggedInUserId) {
        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);
        IdolGroup idolGroup = idolGroupRepository.findById(idolGroupId)
                .orElseThrow(IdolGroupNotFoundException::new);

        Optional<CommunityFavorites> favorite = communityFavoritesRepository.findByUserAndIdolGroup(user, idolGroup);

        if (favorite.isPresent()) {
            communityFavoritesRepository.delete(favorite.get());
        } else {
            CommunityFavorites newFavorite = new CommunityFavorites(user, idolGroup);
            communityFavoritesRepository.save(newFavorite);
        }

    }

    @Override
    public Page<FavoritesGetResponse> getFavorites(Long loggedInUserId, Pageable pageable) {
        User user = userRepository.findById(loggedInUserId)
                .orElseThrow(UserNotFoundException::new);

        Page<CommunityFavorites> favoritesPage = communityFavoritesRepository.findByUser(user, pageable);

        return favoritesPage.map(favorite -> new FavoritesGetResponse(
                        favorite.getIdolGroup().getId(),
                        favorite.getIdolGroup().getName(),
                        favorite.getIdolGroup().getProfile()
        ));
    }

    @Override
    public Page<IdolGroupGetResponse> getIdolGroups(Pageable pageable) {
        return idolGroupRepository.findAll(pageable)
                .map(idolGroup -> new IdolGroupGetResponse(
                        idolGroup.getId(),
                        idolGroup.getName(),
                        idolGroup.getProfile()
                ));
    }

    @Override
    public IdolGroupDetailResponse getIdolGroupDetail(Long loggedInUserId, Long idolGroupId) {
        IdolGroup idolGroup = idolGroupRepository.findById(idolGroupId)
                .orElseThrow(IdolGroupNotFoundException::new);

        IdolGroupDetailResponse.IdolGroupDetailResponseBuilder builder = IdolGroupDetailResponse.builder()
                .idolGroupId(idolGroup.getId())
                .name(idolGroup.getName())
                .peopleNum(idolGroup.getPeopleNum())
                .introduction(idolGroup.getIntroduction())
                .profile(idolGroup.getProfile())
                .banner(idolGroup.getBanner());

        if (loggedInUserId != null) {
            User user = userRepository.findById(loggedInUserId)
                    .orElseThrow(UserNotFoundException::new);

            boolean isFavorite  = communityFavoritesRepository.existsByUserAndIdolGroup(user, idolGroup);
            builder.isFavorite(isFavorite);
        }

        return builder.build();
    }
}
