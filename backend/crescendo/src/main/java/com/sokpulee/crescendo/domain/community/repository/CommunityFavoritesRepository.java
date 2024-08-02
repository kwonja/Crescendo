package com.sokpulee.crescendo.domain.community.repository;

import com.sokpulee.crescendo.domain.community.entity.CommunityFavorites;
import com.sokpulee.crescendo.domain.idol.entity.IdolGroup;
import com.sokpulee.crescendo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityFavoritesRepository extends JpaRepository<CommunityFavorites, Long> {

    Optional<CommunityFavorites> findByUserAndIdolGroup(User user, IdolGroup idolGroup);
}
