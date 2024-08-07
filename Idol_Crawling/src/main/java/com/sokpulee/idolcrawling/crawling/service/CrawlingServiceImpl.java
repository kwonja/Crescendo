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

    @Override
    public String getName(Elements infoTableRows, String tag) {
        infoTableRows.select("sup").remove();
        return infoTableRows.select(tag).get(0).html().split("<br>")[0];
    }

    @Override
    public String getImgUrl(Elements infoTableRows) {
        Elements parseImgUrl = infoTableRows.select("td > span > a").select("img");
        String imgUrl = parseImgUrl.get(parseImgUrl.size() - 1).attr("src");
        return imgUrl.replaceAll("/\\d+px-", "/" + "1000" + "px-");
    }

    @Override
    public List<String> getMemberParamList(Elements rows) {
        List<String> memberParamList = new ArrayList<>();
        int memberIdx = getMemberIdx(rows);

        if (memberIdx == 0) return null;

        Elements members = rows.get(memberIdx).select("td").select("a");

        for (Element member : members) {
            memberParamList.add(member.attr("href"));
        }

        return memberParamList;
    }

    @Override
    public int getMemberIdx(Elements rows) {
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).text().equals("구성원")) return i + 1;
        }

        return 0;
    }

}
