package com.sokpulee.idolcrawling.crawling.controller;

import com.sokpulee.idolcrawling.crawling.service.CrawlingService;
import com.sokpulee.idolcrawling.crawling.service.CrawlingServiceImpl;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CrawlingController {

    private final CrawlingService crawlingService;

    @PostConstruct
    public void init() {
        System.out.println("Hello, World!");
    }

}
