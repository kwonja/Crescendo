package com.sokpulee.crescendo.domain.idol.repository;

import com.sokpulee.crescendo.domain.idol.entity.Idol;
import com.sokpulee.crescendo.domain.idol.entity.IdolGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IdolRepository extends JpaRepository<Idol, Long> {

    List<Idol> findByIdolGroup(IdolGroup idolGroup);


    @Query("SELECT i FROM Idol i JOIN FETCH i.idolGroup WHERE i.id IN :idolIds")
    List<Idol> findByIdIn(@Param("idolIds") List<Long> idolIds);

}
