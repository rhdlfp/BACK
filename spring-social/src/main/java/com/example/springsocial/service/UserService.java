package com.example.springsocial.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.springsocial.model.User;
import com.example.springsocial.repository.UserRepository;

@Service
public class UserService {

	@Value("${app.oauth2.image-upload-directory}")
    private String imageUploadDirectory;

    public String saveImage(MultipartFile file, Long userId) throws IOException {
        String fileName = userId + "_" + generateFileName(file.getOriginalFilename());
        Path imagePath = Paths.get(imageUploadDirectory, fileName);
        Files.write(imagePath, file.getBytes());
        return fileName;
    }

    private String generateFileName(String originalFileName) {
        String uuid = UUID.randomUUID().toString();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return uuid + extension;
    }
    
    @Autowired
    private UserRepository userRepository; // UserRepository는 사용자 데이터베이스 조작을 위한 리포지터리라고 가정합니다.

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null); // 현재의 UserRepository에 따라 사용자를 조회합니다.
    }
    
}
