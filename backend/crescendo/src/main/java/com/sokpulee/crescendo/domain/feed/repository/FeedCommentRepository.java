package com.sokpulee.crescendo.domain.feed.repository;

import com.sokpulee.crescendo.domain.feed.entity.FeedComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long>, FeedCommentCustomRepository {
}
