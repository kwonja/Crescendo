package com.sokpulee.idolcrawling.crawling.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class IdolGroupDto {
    private String name;
    private int peopleNum;
    private String introduction;
    private String profile;
    private String banner;
    private String gender;
    private List<String> memberParamList;
}
