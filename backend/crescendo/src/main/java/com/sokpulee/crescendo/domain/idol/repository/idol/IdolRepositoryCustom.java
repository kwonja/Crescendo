package com.sokpulee.crescendo.domain.idol.repository.idol;

import com.sokpulee.crescendo.domain.idol.entity.Idol;
import com.sokpulee.crescendo.domain.idol.enums.Gender;

import java.util.List;

public interface IdolRepositoryCustom {

    List<Idol> findAllIdolsByGender(Gender gender);
}
