package com.sokpulee.idolcrawling.crawling.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class IdolDto {
    private String idolGroupName;
    private String name;
    private String gender;
    private String profile1;
    private String profile2;
}
