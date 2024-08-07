package com.sokpulee.idolcrawling.crawling.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CrawlingServiceImpl implements CrawlingService {

    @Value("${BASE_CRAWLING_URL}")
    private String baseCrawlingUrl;

    @Override
    public Elements getTables(String param) throws IOException {
        String a = baseCrawlingUrl + param;
        Document html = Jsoup.connect(baseCrawlingUrl + param).get();
        return html.select("body > div > div > div > main > div > div > div > table");
    }

}
