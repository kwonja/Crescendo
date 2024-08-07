package com.sokpulee.idolcrawling.crawling.service;

import org.jsoup.select.Elements;

import java.util.List;

public interface CrawlingService {

    Elements getTables(String param) throws Exception;

    List<String> getIdolGroupParamList(Elements tables, String tag) throws Exception ;

    Elements getInfoTableRows(String param) throws Exception;

    String getName(Elements infoTableRows, String tag) throws Exception;

    String getImgUrl(Elements infoTableRows) throws Exception;

    List<String> getMemberParamList(Elements rows) throws Exception;

    int getMemberIdx(Elements rows) throws Exception;

}
