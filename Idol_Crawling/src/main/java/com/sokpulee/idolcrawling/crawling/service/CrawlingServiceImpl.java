package com.sokpulee.idolcrawling.crawling.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CrawlingServiceImpl implements CrawlingService {

    @Value("${BASE_CRAWLING_URL}")
    private String baseCrawlingUrl;

    @Override
    public Elements getTables(String param) throws IOException {
        Document html = Jsoup.connect(baseCrawlingUrl + param).get();
        return html.select("body > div > div > div > main > div > div > div > table");
    }

    @Override
    public List<String> getIdolGroupParamList(Elements tables, String tag) {
        List<String> idolGroupParamList = new ArrayList<>();

        for (Element table : tables) {
            Elements rows = table.select("tbody > tr");

            for (int i = 1; i < rows.size(); i++) {
                Element row = rows.get(i);
                idolGroupParamList.add(row.select(tag).attr("href"));
            }

        }

        return idolGroupParamList;
    }

    @Override
    public Elements getInfoTableRows(String param) throws IOException {
        return getTables(param).get(0).select("tbody > tr");
    }

}
