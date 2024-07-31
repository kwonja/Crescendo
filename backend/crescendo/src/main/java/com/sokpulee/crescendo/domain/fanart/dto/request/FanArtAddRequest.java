package com.sokpulee.crescendo.domain.fanart.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class FanArtAddRequest {
    private String title;

    private String content;

    private List<MultipartFile> imageList;

    private Long idolGroupId;

    public FanArtAddRequest(String title, String content, List<MultipartFile> imageList, Long idolGroupId) {
        this.title = title;
        this.content = content;
        this.imageList = imageList;
        this.idolGroupId = idolGroupId;
    }

    public List<MultipartFile> getImageList() {
        if (imageList == null) {
            imageList = new ArrayList<>();
        }
        return imageList;
    }
}
