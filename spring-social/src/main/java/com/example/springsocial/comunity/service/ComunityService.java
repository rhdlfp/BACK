package com.example.springsocial.comunity.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.springsocial.comunity.Comunity;
import com.example.springsocial.comunity.repository.ComunityRepository;
import com.example.springsocial.exception.ResourceNotFoundException;
import com.example.springsocial.util.PagingUtil;
import com.example.springsocial.comunity.comment.Comment;
import com.example.springsocial.comunity.comment.repository.CommentRepository;
import com.example.springsocial.comunity.heart.Heart;
import com.example.springsocial.comunity.heart.repository.HeartRepository;

@Service
public class ComunityService {

	@Autowired
	private ComunityRepository comunityRepository;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private HeartRepository heartRepository;
	
	public int findAllCount() {
		return (int) comunityRepository.count();
	}
	
	// get paging boards data
	public ResponseEntity<Map> getPagingBoard(Integer p_num) {
		Map result = null;
		
		PagingUtil pu = new PagingUtil(p_num, 5, 5); // ($1:표시할 현재 페이지, $2:한페이지에 표시할 글 수, $3:한 페이지에 표시할 페이지 버튼의 수 )
		List<Comunity> list = comunityRepository.findFromTo(pu.getObjectStartNum(), pu.getObjectCountPerPage());
		pu.setObjectCountTotal(findAllCount());
		pu.setCalcForPaging();
		
		System.out.println("p_num : "+p_num);
		System.out.println(pu.toString());
		
		if (list == null || list.size() == 0) {
			return null;
		}
		
		result = new HashMap<>();
		result.put("pagingData", pu);
		result.put("list", list);
		
		return ResponseEntity.ok(result);
	}	

	// get boards data
	
	public List<Comunity> getAllBoard() {
		return comunityRepository.findAll();
	}


	public Comunity createBoard(Comunity comunity) {
		return comunityRepository.save(comunity);
	}
	
	public ResponseEntity<Comunity> getBoard(Long id){
		Comunity comunity = comunityRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Not exist Board Data by id : ["+id+"]", null, null));
		return ResponseEntity.ok(comunity);
	}
	
	public ResponseEntity<Comunity> updateBoard(Long id, Comunity updatedBoard) {
		Comunity comunity = comunityRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Not exist Board Data by id : ["+id+"]", null, null));
		comunity.setStarpoint(updatedBoard.getStarpoint());
		comunity.setType(updatedBoard.getType());
		comunity.setTitle(updatedBoard.getTitle());
		comunity.setContents(updatedBoard.getContents());
		comunity.setUpdatedTime(new Date());
		
		Comunity endUpdatedBoard = comunityRepository.save(comunity);
		return ResponseEntity.ok(endUpdatedBoard);
	}
	
	public ResponseEntity<Map<String, Boolean>> deleteBoard(Long id){
		Comunity comunity = comunityRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Not exist Board Data by id : ["+id+"]", null, null));
		
	    // Comunity와 연관된 Comment 레코드 삭제
	    List<Comment> comments = commentRepository.findAllByComunity(comunity);
	    commentRepository.deleteAll(comments);

	    // Comunity와 연관된 Heart 레코드 삭제
	    List<Heart> hearts = heartRepository.findAllByComunity(comunity);
	    for (Heart heart : hearts) {
	        heartRepository.delete(heart);
	    }
		
		comunityRepository.delete(comunity);
		Map<String, Boolean> response = new HashMap<>();
		response.put("Deleted Board Data by id : [\"+id+\"]", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}

}