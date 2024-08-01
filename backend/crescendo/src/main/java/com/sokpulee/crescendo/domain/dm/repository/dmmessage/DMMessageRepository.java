package com.sokpulee.crescendo.domain.dm.repository.dmmessage;

import com.sokpulee.crescendo.domain.dm.entity.DmMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DMMessageRepository extends JpaRepository<DmMessage, Long>, DMMessageRepositoryCustom {
}
