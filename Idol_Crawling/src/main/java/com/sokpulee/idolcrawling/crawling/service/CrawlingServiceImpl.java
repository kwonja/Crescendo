package com.sokpulee.idolcrawling.crawling.service;

import com.sokpulee.idolcrawling.crawling.repository.CrawlingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CrawlingServiceImpl implements CrawlingService {

    private final CrawlingRepository crawlingRepository;

}
