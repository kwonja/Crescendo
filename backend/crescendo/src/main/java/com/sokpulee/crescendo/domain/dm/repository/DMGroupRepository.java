package com.sokpulee.crescendo.domain.dm.repository;

import com.sokpulee.crescendo.domain.dm.entity.DmGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DMGroupRepository extends JpaRepository<DmGroup, Long> {

    @Query("SELECT g FROM DmGroup g JOIN g.dmParticipantList p1 JOIN g.dmParticipantList p2 " +
            "WHERE p1.user.id = :userId AND p2.user.id = :opponentId")
    Optional<DmGroup> findDmGroupByParticipants(@Param("userId") Long userId, @Param("opponentId") Long opponentId);

    @Query("SELECT g FROM DmGroup g JOIN g.dmParticipantList p WHERE p.user.id = :userId")
    List<DmGroup> findAllByUserId(@Param("userId") Long userId);
}
