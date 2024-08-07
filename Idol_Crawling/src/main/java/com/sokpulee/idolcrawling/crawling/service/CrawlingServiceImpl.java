package com.sokpulee.idolcrawling.crawling.service;

import com.sokpulee.idolcrawling.crawling.dto.IdolDto;
import com.sokpulee.idolcrawling.crawling.dto.IdolGroupDto;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CrawlingServiceImpl implements CrawlingService {

    @Value("${BASE_CRAWLING_URL}")
    private String baseCrawlingUrl;

    @Value("${BOY_GROUP_LIST_PARAM}")
    private String boyGroupListParam;

    @Value("${GIRL_GROUP_LIST_PARAM}")
    private String girlGroupListParam;

    @Override
    public Elements getTables(String param) throws Exception {
        Document html = Jsoup.connect(baseCrawlingUrl + param).get();
        return html.select("body > div > div > div > main > div > div > div > table");
    }

    @Override
    public List<String> getIdolGroupParamList(Elements tables, String tag) throws Exception {
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
    public Elements getInfoTableRows(String param) throws Exception {
        return getTables(param).get(0).select("tbody > tr");
    }

    @Override
    public String getName(Elements infoTableRows, String tag) throws Exception {
        infoTableRows.select("sup").remove();
        return infoTableRows.select(tag).get(0).html().split("<br>")[0];
    }

    @Override
    public String getImgUrl(Elements infoTableRows) throws Exception {
        Elements parseImgUrl = infoTableRows.select("td > span > a").select("img");
        String imgUrl = parseImgUrl.get(parseImgUrl.size() - 1).attr("src");
        return imgUrl.replaceAll("/\\d+px-", "/" + "1000" + "px-");
    }

    @Override
    public int getMemberIdx(Elements rows) throws Exception {
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).text().equals("구성원")) return i + 1;
        }

        return 0;
    }

    @Override
    public List<String> getMemberParamList(Elements rows) throws Exception {
        int memberIdx = getMemberIdx(rows);
        if (memberIdx == 0) return null;
        List<String> memberParamList = new ArrayList<>();
        Elements members = rows.get(memberIdx).select("td").select("a");

        for (Element member : members) {
            memberParamList.add(member.attr("href"));
        }

        return memberParamList;
    }

    @Override
    public List<IdolGroupDto> getIdolGroupList(String param, String groupTag, String nameTag, String gender) throws Exception {
        List<IdolGroupDto> idolGroupDtoList = new ArrayList<>();

        try {
            List<String> idolGroupParamList = getIdolGroupParamList(getTables(param), groupTag);

            for (String idolGroupParam : idolGroupParamList) {
                try {
                    Elements rows = getInfoTableRows(idolGroupParam);
                    String name = getName(rows, nameTag);
                    String imgUrl = getImgUrl(rows);
                    List<String> memberParamList = getMemberParamList(rows);
                    if (memberParamList == null) continue;
                    idolGroupDtoList.add(new IdolGroupDto(name, memberParamList.size(), name, imgUrl, imgUrl, gender, memberParamList));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return idolGroupDtoList;
    }

    @Override
    public List<IdolDto> getIdolMemberList(List<IdolGroupDto> idolGroupDtoList, String nameTag) throws Exception {
        List<IdolDto> idolDtoList = new ArrayList<>();

        for (IdolGroupDto idolGroupDto : idolGroupDtoList) {
            for (String memberParam : idolGroupDto.getMemberParamList()) {
                try {
                    Elements rows = getInfoTableRows(memberParam);
                    String name = getName(rows, nameTag);
                    String imgUrl = getImgUrl(rows);
                    idolDtoList.add(new IdolDto(idolGroupDto.getName(), name, idolGroupDto.getGender(), imgUrl, imgUrl));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return idolDtoList;
    }

    @Override
    public void crawlingIdol() throws Exception {
        String boyGroupTag = "td > b > a";
        String girlGroupTag = "td > a";
        String idolGroupNameTag = "th > div > span > big";
        String idolNameTag = "th > div > span";

        List<IdolGroupDto> idolGroupDtoList = new ArrayList<>();
        idolGroupDtoList.addAll(getIdolGroupList(boyGroupListParam, boyGroupTag, idolGroupNameTag, "M"));
        idolGroupDtoList.addAll(getIdolGroupList(girlGroupListParam, girlGroupTag, idolGroupNameTag, "F"));
        List<IdolDto> idolDtoList = new ArrayList<>(getIdolMemberList(idolGroupDtoList, idolNameTag));
    }

}
