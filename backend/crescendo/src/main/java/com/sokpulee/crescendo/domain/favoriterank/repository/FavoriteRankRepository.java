package com.sokpulee.crescendo.domain.favoriterank.repository;

import com.sokpulee.crescendo.domain.favoriterank.entity.FavoriteRank;
import com.sokpulee.crescendo.domain.idol.entity.Idol;
import com.sokpulee.crescendo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FavoriteRankRepository extends JpaRepository<FavoriteRank, Long> {

    @Query("SELECT fr.idol, fr.favoriteIdolImagePath, COUNT(frv) as voteCount " +
            "FROM FavoriteRank fr " +
            "JOIN FavoriteRankVoting frv ON fr.favoriteRankId = frv.favoriteRank.favoriteRankId " +
            "WHERE fr.createdAt >= :startDate AND fr.createdAt < :endDate " +
            "GROUP BY fr.idol, fr.favoriteIdolImagePath " +
            "ORDER BY fr.idol, voteCount DESC")
    List<Object[]> findFavoriteImagesWithVoteCountForMonth(@Param("startDate") LocalDateTime startDate,
                                                           @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(fr) > 0 FROM FavoriteRank fr WHERE fr.user = :user AND fr.createdAt BETWEEN :startDate AND :endDate")
    boolean existsByUserAndCreatedAtBetween(User user, LocalDateTime startDate, LocalDateTime endDate);
}
