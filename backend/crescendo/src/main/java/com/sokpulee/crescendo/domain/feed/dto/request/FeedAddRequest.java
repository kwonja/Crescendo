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

    private String title;

    private String content;

    private List<MultipartFile> imageList;

    private List<String> tagList;

    private Long idolGroupId;

    public FeedAddRequest(String title, String content, List<MultipartFile> imageList, List<String> tagList, Long idolGroupId) {
        this.title = title;
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
}
