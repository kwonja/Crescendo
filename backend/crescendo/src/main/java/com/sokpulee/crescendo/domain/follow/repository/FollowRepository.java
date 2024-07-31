package com.sokpulee.crescendo.domain.follow.repository;

import com.sokpulee.crescendo.domain.follow.entity.Follow;
import com.sokpulee.crescendo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    long countByFollowing(User user);
    long countByFollower(User user);
    boolean existsByFollowingAndFollower(User following, User follower);

    List<Follow> findByFollower(User user);

    List<Follow> findByFollowing(User user);
}
