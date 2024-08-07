package com.sokpulee.crescendo.domain.feed.dto.request;

import com.sokpulee.crescendo.domain.feed.entity.Feed;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class FeedAddRequest {

    private String content;

    private List<MultipartFile> imageList;

    private List<String> tagList;

    private Long idolGroupId;

    public FeedAddRequest(String content, List<MultipartFile> imageList, List<String> tagList, Long idolGroupId) {
        this.content = content;
        this.imageList = imageList;
        this.tagList = tagList;
        this.idolGroupId = idolGroupId;
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
