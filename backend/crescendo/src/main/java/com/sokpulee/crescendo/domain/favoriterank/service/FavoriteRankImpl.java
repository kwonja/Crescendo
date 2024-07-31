package com.sokpulee.crescendo.domain.favoriterank.service;

import com.sokpulee.crescendo.domain.favoriterank.repository.FavoriteRankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class FavoriteRankImpl implements FavoriteRank {

    private final FavoriteRankRepository favoriteRankRepository;
}
