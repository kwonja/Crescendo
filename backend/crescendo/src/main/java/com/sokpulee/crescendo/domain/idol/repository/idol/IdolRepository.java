package com.sokpulee.crescendo.domain.idol.repository.idol;

import com.sokpulee.crescendo.domain.idol.entity.Idol;
import com.sokpulee.crescendo.domain.idol.entity.IdolGroup;
import com.sokpulee.crescendo.domain.idol.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IdolRepository extends JpaRepository<Idol, Long>, IdolRepositoryCustom{

    List<Idol> findByIdolGroup(IdolGroup idolGroup);

    @Query("SELECT i FROM Idol i JOIN FETCH i.idolGroup WHERE i.id IN :idolIds")
    List<Idol> findByIdIn(@Param("idolIds") List<Long> idolIds);

    @Query("SELECT i FROM Idol i JOIN FETCH i.idolGroup WHERE i.gender = :gender")
    List<Idol> findAllWithGroup(@Param("gender") Gender gender);
}
