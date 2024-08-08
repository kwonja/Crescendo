package com.sokpulee.crescendo.domain.fanart.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FanArtUpdateRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private List<MultipartFile> imageList;

    public FanArtUpdateRequest(String title, String content, List<MultipartFile> imageList) {
        this.title = title;
        this.content = content;
        this.imageList = imageList;
    }

    public List<MultipartFile> getImageList() {
        if (imageList == null) {
            imageList = new ArrayList<>();
        }
        return imageList;
    }
}
