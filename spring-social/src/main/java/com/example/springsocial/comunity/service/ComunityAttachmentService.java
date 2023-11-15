package com.example.springsocial.comunity.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.springsocial.comunity.ComunityAttachment;
import com.example.springsocial.comunity.ComunityAttachmentType;
import com.example.springsocial.comunity.ComunityFileStore;
import com.example.springsocial.comunity.Comunity;
import com.example.springsocial.comunity.repository.ComunityAttachmentRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ComunityAttachmentService {
	private final ComunityAttachmentRepository attachmentRepository;
	private final ComunityFileStore fileStore;
	
	
	
	public List<ComunityAttachment> saveAttachment(Map<ComunityAttachmentType,List<MultipartFile>>multipartFileListMap) throws IOException{
	    List<MultipartFile> imageFiles = multipartFileListMap.getOrDefault(ComunityAttachmentType.IMAGE, Collections.emptyList());
	    List<MultipartFile> generalFiles = multipartFileListMap.getOrDefault(ComunityAttachmentType.GENERAL, Collections.emptyList());

	    List<ComunityAttachment> imageAttachments = fileStore.storeFiles(imageFiles, ComunityAttachmentType.IMAGE);
	    List<ComunityAttachment> generalAttachments = fileStore.storeFiles(generalFiles, ComunityAttachmentType.GENERAL);

	    List<ComunityAttachment> result = Stream.concat(imageAttachments.stream(), generalAttachments.stream())
	            .collect(Collectors.toList());

	    return result;
	}

		
	public Map<ComunityAttachmentType, List<ComunityAttachment>> findAttachments() {
	        List<ComunityAttachment> attachments = attachmentRepository.findAll();
	        Map<ComunityAttachmentType, List<ComunityAttachment>> result = attachments.stream()
	                .collect(Collectors.groupingBy(ComunityAttachment::getAttachmentType));

	        return result;
	    }
	
	public ComunityAttachment save(ComunityAttachment attachment) throws IOException{
		return attachmentRepository.save(attachment);
	    
	}
	 
	 
}
