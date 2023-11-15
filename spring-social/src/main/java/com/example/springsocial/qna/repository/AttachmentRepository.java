package com.example.springsocial.qna.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springsocial.qna.Attachment;
import com.example.springsocial.qna.AttachmentType;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findByQnaNoAndAttachmentType(Long qnaNo, AttachmentType attachmentType);
}

