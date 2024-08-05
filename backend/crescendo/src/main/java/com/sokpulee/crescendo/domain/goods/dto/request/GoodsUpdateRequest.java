package com.sokpulee.crescendo.domain.goods.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GoodsUpdateRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private List<MultipartFile> imageList;

    @NotNull
    private Long idolGroupId;

    public GoodsUpdateRequest(String title, String content, List<MultipartFile> imageList, Long idolGroupId) {
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
