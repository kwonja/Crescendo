package com.sokpulee.crescendo.domain.feed.repository;

import com.sokpulee.crescendo.domain.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long>, FeedCustomRepository {

}
