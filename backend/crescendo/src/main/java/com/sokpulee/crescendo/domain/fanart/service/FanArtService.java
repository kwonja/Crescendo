package com.sokpulee.crescendo.domain.fanart.service;

import com.sokpulee.crescendo.domain.fanart.dto.request.FanArtAddRequest;

public interface FanArtService {
    void addFanArt(Long loggedInUserId, FanArtAddRequest fanArtAddRequest);
}
