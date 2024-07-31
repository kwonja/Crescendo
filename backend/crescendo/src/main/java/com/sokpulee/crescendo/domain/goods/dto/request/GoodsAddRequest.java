package com.sokpulee.crescendo.domain.goods.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class GoodsAddRequest {
    private String title;

    private String content;

    private List<MultipartFile> imageList;

    private long idolGroupId;

    public GoodsAddRequest(String title, String content, List<MultipartFile> imageList, long idolGroupId) {
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
