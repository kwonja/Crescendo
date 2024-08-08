package com.sokpulee.idolcrawling.crawling.service;

import com.sokpulee.idolcrawling.crawling.dto.IdolDto;
import com.sokpulee.idolcrawling.crawling.dto.IdolGroupDto;
import org.jsoup.select.Elements;

import java.util.List;

public interface CrawlingService {

    Elements getTables(String param) throws Exception;

    List<String> getIdolGroupParamList(Elements tables, String tag) throws Exception ;

    Elements getInfoTableRows(String param) throws Exception;

    String getName(Elements infoTableRows, String tag) throws Exception;

    String getImgUrl(Elements infoTableRows) throws Exception;

    int getMemberIdx(Elements rows) throws Exception;

    List<String> getMemberParamList(Elements rows) throws Exception;

    List<IdolGroupDto> getIdolGroupList(String param, String groupTag, String nameTag, String gender) throws Exception;

    List<IdolDto> getIdolMemberList(List<IdolGroupDto> idolGroupDtoList, String nameTag) throws Exception;

    void crawlingIdol() throws Exception;

    void saveCrawlingData(List<IdolGroupDto> idolGroupDtoList, List<IdolDto> idolDtoList) throws Exception;

}
