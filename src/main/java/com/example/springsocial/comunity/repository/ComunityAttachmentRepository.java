package com.example.springsocial.comunity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springsocial.comunity.ComunityAttachment;
import com.example.springsocial.comunity.ComunityAttachmentType;

public interface ComunityAttachmentRepository extends JpaRepository<ComunityAttachment, Long> {
    List<ComunityAttachment> findByComunityIdAndAttachmentType(Long comunityId, ComunityAttachmentType attachmentType);
}
