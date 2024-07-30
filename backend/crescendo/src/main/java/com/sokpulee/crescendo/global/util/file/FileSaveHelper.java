package com.sokpulee.crescendo.global.util.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileSaveHelper {
    void deleteFile(String filePath);

    String saveUserProfile(MultipartFile profileImage);
}
