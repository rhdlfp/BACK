package com.example.springsocial.comunity.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springsocial.comunity.Comunity;
import com.example.springsocial.comunity.comment.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByComunity(Comunity Comunity);

	
}
