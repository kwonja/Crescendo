package com.sokpulee.idolcrawling.crawling.service;

import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public interface CrawlingService {

    Elements getTables(String param) throws IOException;

    List<String> getIdolGroupParamList(Elements tables, String tag);

    Elements getInfoTableRows(String param) throws IOException;

    String getName(Elements infoTableRows, String tag);

}
