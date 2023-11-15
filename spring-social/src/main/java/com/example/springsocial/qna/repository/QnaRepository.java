package com.example.springsocial.qna.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.springsocial.qna.Qna;

public interface QnaRepository extends JpaRepository<Qna, Long> {
	
	List<Qna> findByUserid(Long userid);

	public final static String SELECT_QNA_LIST_PAGED = ""
			+ "SELECT "
			+ "no,"
			+ "type,"
			+ "title,"
			+ "contents,"
			+ "userid,"
			+ "nickname,"
			+ "created_time,"
			+ "updated_time"
			+ " FROM qna WHERE 0 < no "
			+ "ORDER BY no DESC LIMIT ?1, ?2";
	
	
	@Query(value = SELECT_QNA_LIST_PAGED, nativeQuery = true)
	List<Qna> findFromTo(
			final Integer objectStartNum,
			final Integer objectEndNum);

	

}
