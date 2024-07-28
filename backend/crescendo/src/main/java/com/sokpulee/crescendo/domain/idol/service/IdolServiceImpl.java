package com.sokpulee.crescendo.domain.idol.service;

import com.sokpulee.crescendo.domain.idol.repository.IdolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class IdolServiceImpl implements IdolService {

    private final IdolRepository idolRepository;

}
