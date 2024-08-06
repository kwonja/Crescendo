package com.sokpulee.crescendo.domain.challenge.repository;

import com.sokpulee.crescendo.domain.challenge.entity.DanceChallengeJoin;
import com.sokpulee.crescendo.domain.challenge.entity.DanceChallengeJoinLike;
import com.sokpulee.crescendo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DanceChallengeJoinLikeRepository extends JpaRepository<DanceChallengeJoinLike, Long> {
    Optional<DanceChallengeJoinLike> findByDanceChallengeJoinAndUser(DanceChallengeJoin danceChallengeJoin, User user);
}
