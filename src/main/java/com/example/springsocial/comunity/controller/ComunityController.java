package com.example.springsocial.comunity.controller;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.springsocial.comunity.Comunity;
import com.example.springsocial.comunity.ComunityAttachment;
import com.example.springsocial.comunity.ComunityAttachmentType;
import com.example.springsocial.comunity.ComunityFileStore;
import com.example.springsocial.comunity.repository.ComunityAttachmentRepository;
import com.example.springsocial.comunity.repository.ComunityRepository;
import com.example.springsocial.comunity.service.ComunityAttachmentService;
import com.example.springsocial.comunity.service.ComunityService;
import com.example.springsocial.exception.ResourceNotFoundException;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class ComunityController {
	
	@Autowired
	private ComunityService comunityService;
	@Autowired
	private ComunityAttachmentService attachmentService;
	@Autowired
	private ComunityRepository comunityRepository;
	@Autowired
	private ComunityAttachmentRepository attachmentRepository;
	@Autowired
	private ComunityFileStore fileStore;

	@GetMapping("/comunity")
	public ResponseEntity<Map> getAllBoards(@RequestParam(value = "p_num", required=false) Integer p_num) {
		if (p_num == null || p_num <= 0) p_num = 1;
		
		return comunityService.getPagingBoard(p_num);
	}
  
//	@PostMapping("/comunity")
//	public Comunity createBoard(@RequestBody Comunity comunity) {
//		System.out.println("@PostMapping(\"/comunity\")");
//		System.out.println(comunity.toString());
//		return comunityService.createBoard(comunity);
//	}
	
	@PostMapping(value = "/comunity")
	public Comunity createBoard(@ModelAttribute Comunity comunity,
	                     @RequestPart(value = "file", required = false) List<MultipartFile> files) throws IOException {
		Comunity savedComunity = comunityService.createBoard(comunity);
	    if (files != null && !files.isEmpty()) {
	        List<ComunityAttachment> attachments = new ArrayList<>();
	        for (MultipartFile singleFile : files) {
	        	
	        	Map<ComunityAttachmentType, List<MultipartFile>> fileMap = Collections.singletonMap(ComunityAttachmentType.IMAGE, Collections.singletonList(singleFile));
	            List<ComunityAttachment> singleFileAttachments = attachmentService.saveAttachment(fileMap);
	            attachments.addAll(singleFileAttachments);

	            for (ComunityAttachment attachment : singleFileAttachments) {
	            	Long userid = savedComunity.getUserid();
	            	System.out.println("savedQna.getUserid(): " + userid);
	                String filename = attachment.getOriginFilename();
	                System.out.println(filename);
	                String filepath = attachment.getStoreFilename();
	                System.out.println(filepath);

	                attachment.setComunity(savedComunity);
	                savedComunity.getAttachedFiles().add(attachment);
	                attachmentService.save(attachment);
	            }

	            System.out.println(singleFile.getOriginalFilename());
	    
	        }

	        System.out.println(comunity.toString());
	        return savedComunity;
	    }
	    return null; 
	    }
	
	@ResponseBody
	@GetMapping("/comunity/images/{storeFilename}")
	public ResponseEntity<Resource> processImg(@PathVariable String storeFilename) throws MalformedURLException {
		
	    Resource resource = new UrlResource("file:" + fileStore.createPath(storeFilename, ComunityAttachmentType.IMAGE));

	    // Check if the resource exists
	    if (resource.exists() && resource.isReadable()) {
	        return ResponseEntity.ok()
	                .contentType(MediaType.IMAGE_JPEG) // You can set the appropriate content type based on the image type
	                .body(resource);
	    } else {
	        // Handle the case where the resource does not exist or is not readable
	        // You can return a default image or an error message
	        return ResponseEntity.notFound().build();
	    }
	}
	
	@GetMapping("/comunity/{id}")
	public ResponseEntity<Comunity> getBoardByid(@PathVariable Long id) {
		return comunityService.getBoard(id);
	}
	
//	@PutMapping("/comunity/{id}")
//	public ResponseEntity<Comunity> updateBoardByid(@PathVariable Long id, @RequestBody Comunity comunity) {
//		return comunityService.updateBoard(id, comunity);
//	}
	
	@PutMapping("/comunity/{id}")
	public ResponseEntity<Comunity> updateBoardByid(
	    @PathVariable Long id,
	    @ModelAttribute Comunity comunity,
	    @RequestPart(value = "file", required = false) List<MultipartFile> files
	) throws IOException {
	    Comunity existingComunity = comunityRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Comunity", "id", id));

	    existingComunity.setStarpoint(comunity.getStarpoint());
	    existingComunity.setType(comunity.getType());
	    existingComunity.setTitle(comunity.getTitle());
	    existingComunity.setContents(comunity.getContents());

	    // 기존 첨부 파일 삭제 (데이터베이스 레코드만 삭제)
	    List<ComunityAttachmentType> attachmentTypes = Arrays.asList(ComunityAttachmentType.values());
	    for (ComunityAttachmentType type : attachmentTypes) {
	        List<ComunityAttachment> existingAttachments = attachmentRepository.findByComunityIdAndAttachmentType(id, type);
	        for (ComunityAttachment existingAttachment : existingAttachments) {
	            // 데이터베이스에서 레코드만 삭제
	            attachmentRepository.delete(existingAttachment);
	        }
	    }

	    if (files != null && !files.isEmpty()) {
	        List<ComunityAttachment> attachments = new ArrayList<>();

	        for (MultipartFile singleFile : files) {
	            Map<ComunityAttachmentType, List<MultipartFile>> fileMap = Collections.singletonMap(ComunityAttachmentType.IMAGE, Collections.singletonList(singleFile));
	            List<ComunityAttachment> singleFileAttachments = attachmentService.saveAttachment(fileMap);
	            attachments.addAll(singleFileAttachments);

	            for (ComunityAttachment attachment : singleFileAttachments) {
	                Long userid = existingComunity.getUserid();
	                attachment.setComunity(existingComunity);
	                existingComunity.getAttachedFiles().add(attachment);
	                attachmentService.save(attachment);
	            }
	        }
	    }

	    final Comunity updatedComunity = comunityRepository.save(existingComunity);
	    return ResponseEntity.ok(updatedComunity);
	}




	
	@DeleteMapping("comunity/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteBoardByid(@PathVariable Long id){
		return comunityService.deleteBoard(id);
	}
	
    @PutMapping("/comunity/incrementCounts/{id}")
    public ResponseEntity<Comunity> incrementCounts(@PathVariable(value = "id") Long id) {
        Comunity comunity = comunityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comunity", "id", id));

        comunity.setCounts(comunity.getCounts() + 1); // counts 값을 1씩 증가시킴

        final Comunity updatedComunity = comunityRepository.save(comunity);
        return ResponseEntity.ok(updatedComunity);
    }
	
}
