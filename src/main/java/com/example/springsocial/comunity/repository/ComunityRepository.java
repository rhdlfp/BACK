package com.example.springsocial.comunity.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.springsocial.comunity.Comunity;
import com.example.springsocial.qna.Qna;

public interface ComunityRepository extends JpaRepository<Comunity, Long> {
	
	List<Comunity> findByUserid(Long userid);

	public final static String SELECT_BOARD_LIST_PAGED = ""
			+ "SELECT "
			+ "id,"
			+ "type,"
			+ "title,"
			+ "contents,"
			+ "userid,"
			+ "nickname,"
			+ "created_time,"
			+ "updated_time,"
			+ "counts,"
			+ "starpoint"
			+ " FROM comunity WHERE 0 < id "
			+ "ORDER BY id DESC LIMIT ?1, ?2";
	
	
	@Query(value = SELECT_BOARD_LIST_PAGED, nativeQuery = true)
	List<Comunity> findFromTo(
			final Integer objectStartNum,
			final Integer objectEndNum);

	

}
