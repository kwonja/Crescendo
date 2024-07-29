package com.sokpulee.crescendo.global.util.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileSaveHelper {
    void deleteUserProfile(String profilePath);

    String saveUserProfile(MultipartFile profileImage);
}
