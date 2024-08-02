package com.sokpulee.crescendo.domain.community.service;

import com.sokpulee.crescendo.domain.community.entity.CommunityFavorites;
import com.sokpulee.crescendo.domain.community.repository.CommunityFavoritesRepository;
import com.sokpulee.crescendo.domain.idol.entity.IdolGroup;
import com.sokpulee.crescendo.domain.idol.repository.IdolGroupRepository;
import com.sokpulee.crescendo.domain.user.entity.User;
import com.sokpulee.crescendo.domain.user.repository.UserRepository;
import com.sokpulee.crescendo.global.exception.custom.IdolGroupNotFoundException;
import com.sokpulee.crescendo.global.exception.custom.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class CommunityFavoritesServiceImpl implements CommunityFavoritesService {

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
}
