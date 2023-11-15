package com.example.springsocial.qna.controller;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.springsocial.exception.ResourceNotFoundException;
import com.example.springsocial.qna.Attachment;
import com.example.springsocial.qna.AttachmentType;
import com.example.springsocial.qna.FileStore;
import com.example.springsocial.qna.Qna;
import com.example.springsocial.qna.Service.AttachmentService;
import com.example.springsocial.qna.Service.QnaService;
import com.example.springsocial.qna.repository.AttachmentRepository;
import com.example.springsocial.qna.repository.QnaRepository;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class QnaController {
	
	@Autowired
	private QnaService qnaService;
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private QnaRepository qnaRepository;
	@Autowired
	private AttachmentRepository attachmentRepository;
	@Autowired
	private FileStore fileStore;


	@GetMapping("/qna")
	public ResponseEntity<Map> getAllBoards(@RequestParam(value = "p_num", required=false) Integer p_num) {
		if (p_num == null || p_num <= 0) p_num = 1;
		
		return qnaService.getPagingQna(p_num);
	}


	
	@PostMapping(value = "/qna")
	public Qna createQna(@ModelAttribute Qna qna,
	                     @RequestPart(value = "file", required = false) List<MultipartFile> files) throws IOException {
		Qna savedQna = qnaService.createBoard(qna);
	    if (files != null && !files.isEmpty()) {
	        List<Attachment> attachments = new ArrayList<>();
	        for (MultipartFile singleFile : files) {
	        	
	        	Map<AttachmentType, List<MultipartFile>> fileMap = Collections.singletonMap(AttachmentType.IMAGE, Collections.singletonList(singleFile));
	            List<Attachment> singleFileAttachments = attachmentService.saveAttachment(fileMap);
	            attachments.addAll(singleFileAttachments);

	            for (Attachment attachment : singleFileAttachments) {
	            	Long userid = savedQna.getUserid();
	            	System.out.println("savedQna.getUserid(): " + userid);
	                String filename = attachment.getOriginFilename();
	                System.out.println(filename);
	                String filepath = attachment.getStoreFilename();
	                System.out.println(filepath);

	                attachment.setQna(savedQna);
	                savedQna.getAttachedFiles().add(attachment);
	                attachmentService.save(attachment);
	            }

	            System.out.println(singleFile.getOriginalFilename());
	    
	        }

	        System.out.println(qna.toString());
	        return savedQna;
	    }
	    return null; 
	    }
	
	@ResponseBody
	@GetMapping("/qna/images/{storeFilename}")
	public ResponseEntity<Resource> processImg(@PathVariable String storeFilename) throws MalformedURLException {
		
	    Resource resource = new UrlResource("file:" + fileStore.createPath(storeFilename, AttachmentType.IMAGE));

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
	
	
	
	@GetMapping("/qna/{no}")
	public ResponseEntity<Qna> getBoardByno(@PathVariable Long no) {
		return qnaService.getBoard(no);
	}
	
//	@PutMapping("/qna/{no}")
//	public ResponseEntity<Qna> updateBoardByno(@PathVariable Long no, @ModelAttribute Qna qna) {
//		return qnaService.updateBoard(no, qna);
//	}

	
	@PutMapping("/qna/{no}")
	public ResponseEntity<Qna> updateBoardByno(
	    @PathVariable Long no,
	    @ModelAttribute Qna qna,
	    @RequestPart(value = "file", required = false) List<MultipartFile> files
	) throws IOException {
		Qna existingQna = qnaRepository.findById(no)
	            .orElseThrow(() -> new ResourceNotFoundException("qna", "no", no));

		existingQna.setType(qna.getType());
		existingQna.setTitle(qna.getTitle());
		existingQna.setContents(qna.getContents());

	    // 기존 첨부 파일 삭제 (데이터베이스 레코드만 삭제)
	    List<AttachmentType> attachmentTypes = Arrays.asList(AttachmentType.values());
	    for (AttachmentType type : attachmentTypes) {
	        List<Attachment> existingAttachments = attachmentRepository.findByQnaNoAndAttachmentType(no, type);
	        for (Attachment existingAttachment : existingAttachments) {
	            // 데이터베이스에서 레코드만 삭제
	            attachmentRepository.delete(existingAttachment);
	        }
	    }

	    if (files != null && !files.isEmpty()) {
	        List<Attachment> attachments = new ArrayList<>();

	        for (MultipartFile singleFile : files) {
	            Map<AttachmentType, List<MultipartFile>> fileMap = Collections.singletonMap(AttachmentType.IMAGE, Collections.singletonList(singleFile));
	            List<Attachment> singleFileAttachments = attachmentService.saveAttachment(fileMap);
	            attachments.addAll(singleFileAttachments);

	            for (Attachment attachment : singleFileAttachments) {
	                Long userid = existingQna.getUserid();
	                attachment.setQna(existingQna);
	                existingQna.getAttachedFiles().add(attachment);
	                attachmentService.save(attachment);
	            }
	        }
	    }

	    final Qna updatedQna = qnaRepository.save(existingQna);
	    return ResponseEntity.ok(updatedQna);
	}
	
	
	
	@DeleteMapping("qna/{no}")
	public ResponseEntity<Map<String, Boolean>> deleteBoardByNo(@PathVariable Long no){
		return qnaService.deleteBoard(no);
	}
	
}
