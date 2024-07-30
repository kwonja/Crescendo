package com.sokpulee.crescendo.global.util.file;

import com.sokpulee.crescendo.global.exception.custom.FileDeleteFailException;
import com.sokpulee.crescendo.global.exception.custom.FileSaveFailException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
public class FileSaver implements FileSaveHelper {

    @Value("${image.upload.dir}")
    private String uploadDir;

    private final String PROFILE_DIR = "profile";

    @Override
    public void deleteFile(String filePath) {
        if(filePath == null || filePath.isEmpty()) {
            return;
        }
        File file = new File(uploadDir + File.separator + filePath);

        if (file.exists()) {
            if (!file.delete()) {
                throw new FileDeleteFailException();
            }
        }
    }

    @Override
    public String saveUserProfile(MultipartFile profileImage) {
        return saveFile(profileImage, PROFILE_DIR);
    }
    
    public String saveFile(MultipartFile profileImage, String dir) {
        String realPath = uploadDir + File.separator + dir;
        String today = new SimpleDateFormat("yyMMdd").format(new Date());
        String saveFolder = realPath + File.separator + today;
        File folder = new File(saveFolder);

        // 디렉토리가 존재하지 않으면 생성
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String ext = profileImage.getOriginalFilename().split("\\.")[1];
        String uuid = UUID.randomUUID().toString();

        String newFileName = uuid + "." + ext;

        File savedFile = new File(folder, newFileName);

        try {
            profileImage.transferTo(savedFile);
        } catch (IOException e) {
            throw new FileSaveFailException();
        }

        return dir + File.separator + today + File.separator + newFileName;
    }
}
