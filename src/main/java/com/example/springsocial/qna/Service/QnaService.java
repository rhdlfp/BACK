package com.example.springsocial.qna.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.springsocial.exception.ResourceNotFoundException;
import com.example.springsocial.qna.Qna;
import com.example.springsocial.qna.repository.QnaRepository;
import com.example.springsocial.util.QnaPagingUtil;


@Service
public class QnaService {

	@Autowired
	private QnaRepository qnaRepository;
	
	public int findAllCount() {
		return (int) qnaRepository.count();
	}
	
	// get paging boards data
	public ResponseEntity<Map> getPagingQna(Integer p_num) {
		Map result = null;
		
		QnaPagingUtil pu = new QnaPagingUtil(p_num, 5, 5); // ($1:표시할 현재 페이지, $2:한페이지에 표시할 글 수, $3:한 페이지에 표시할 페이지 버튼의 수 )
		List<Qna> list = qnaRepository.findFromTo(pu.getObjectStartNum(), pu.getObjectCountPerPage());
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
	
	public List<Qna> getAllBoard() {
		return qnaRepository.findAll();
	}


	public Qna createBoard(Qna qna) {
		return qnaRepository.save(qna);
	}
	
	public ResponseEntity<Qna> getBoard(Long no){
		Qna qna = qnaRepository.findById(no)
				.orElseThrow(() -> new ResourceNotFoundException("Not exist Board Data by no : ["+no+"]", null, null));
		return ResponseEntity.ok(qna);
	}
	
	public ResponseEntity<Qna> updateBoard(Long no, Qna updatedBoard) {
		Qna qna = qnaRepository.findById(no)
				.orElseThrow(() -> new ResourceNotFoundException("Not exist Board Data by no : ["+no+"]", null, null));
		qna.setType(updatedBoard.getType());
		qna.setTitle(updatedBoard.getTitle());
		qna.setContents(updatedBoard.getContents());
		qna.setUpdatedTime(new Date());
		
		Qna endUpdatedBoard = qnaRepository.save(qna);
		return ResponseEntity.ok(endUpdatedBoard);
	}
	
	public ResponseEntity<Map<String, Boolean>> deleteBoard(Long no){
		Qna qna = qnaRepository.findById(no)
				.orElseThrow(() -> new ResourceNotFoundException("Not exist Board Data by no : ["+no+"]", null, null));
		qnaRepository.delete(qna);
		Map<String, Boolean> response = new HashMap<>();
		response.put("Deleted Board Data by id : [\"+no+\"]", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}

}