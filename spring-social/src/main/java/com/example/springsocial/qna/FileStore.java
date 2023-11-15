package com.example.springsocial.qna;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;



@Component
public class FileStore {
	@Value("C:\\Temp\\img/")
	private String fileDirPath;

	public List<Attachment> storeFiles(List<MultipartFile> multipartFiles, AttachmentType attachmentType) throws IOException{
        List<Attachment> attachments = new ArrayList<>();
        for(MultipartFile multipartFile : multipartFiles) {
            if(!multipartFile.isEmpty()) {
                attachments.add(storeFile(multipartFile, attachmentType));
            }
        }
        return attachments;
    }
	
	public  Attachment storeFile(MultipartFile multipartFile, AttachmentType attachmentType) throws IOException{
        if(multipartFile.isEmpty()) {
            return null;
        }
        
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFilename(originalFilename);
        
        System.out.println("Storing file: " + storeFilename);
        
        // 이후 저장 전 로그 추가
        multipartFile.transferTo(new File(createPath(storeFilename, attachmentType)));
        System.out.println("File stored at: " + createPath(storeFilename, attachmentType));
        
        return Attachment.builder()
                .originFilename(originalFilename)
                .storePath(storeFilename)
                .attachmentType(attachmentType)
//                .qna(qna) // Qna 객체 추가
                .build();
    }
	public String createPath(String storeFilename, AttachmentType attachmentType) {
        String viaPath = (attachmentType == AttachmentType.IMAGE) ? "" : "generals/";
        System.out.println("AttachmentType51: " + attachmentType);
        System.out.println("viapath " + viaPath);
        return fileDirPath+viaPath+storeFilename;
    }

    private String createStoreFilename(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        String storeFilename = uuid + ext;

        return storeFilename;
    }

    private String extractExt(String originalFilename) {
        int idx = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(idx);
        return ext;
    }

}