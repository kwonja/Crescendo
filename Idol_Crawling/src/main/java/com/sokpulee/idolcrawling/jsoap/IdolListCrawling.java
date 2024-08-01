package com.sokpulee.idolcrawling.jsoap;

import jakarta.annotation.PostConstruct;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class IdolListCrawling {
    @Value("${CRAWLING_URL}")
    private String url;

    @PostConstruct
    public void init() throws IOException {
        Document doc = getUrlHtml(url);

        // System.out.println(doc);
    }

    private Document getUrlHtml(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();

        return doc;
    }
}
