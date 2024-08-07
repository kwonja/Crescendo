package com.sokpulee.crescendo.domain.feed.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FeedUpdateRequest {

    @NotBlank
    private String content;

    private List<MultipartFile> imageList;

    private List<String> tagList;


    public FeedUpdateRequest(String content, List<MultipartFile> imageList, List<String> tagList) {
        this.content = content;
        this.imageList = imageList;
        this.tagList = tagList;
    }

    public List<MultipartFile> getImageList() {
        if (imageList == null) {
            imageList = new ArrayList<>();
        }
        return imageList;
    }

    public List<String> getTagList(){
        if(tagList == null){
            tagList = new ArrayList<>();
        }
        return tagList;
    }
}
