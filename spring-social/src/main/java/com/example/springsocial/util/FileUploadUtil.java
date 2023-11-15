package com.example.springsocial.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileUploadUtil {

    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        File directory = new File(uploadDir);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directory, fileName);
        multipartFile.transferTo(file);
    }
}
