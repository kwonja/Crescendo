package com.sokpulee.idolcrawling.crawling.service;

import org.jsoup.select.Elements;

import java.io.IOException;

public interface CrawlingService {

    Elements getTables(String param) throws IOException;

}
