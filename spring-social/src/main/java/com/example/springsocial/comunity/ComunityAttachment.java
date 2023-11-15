package com.example.springsocial.comunity;


import java.io.File;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@SequenceGenerator(
        name="ATTACHMENT_SEQ_GENERATOR",
        sequenceName = "ATTACHMENT_SEQ"
)
public class ComunityAttachment {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id; 
	 private String originFilename;
	 private String storeFilename;
	 
	 @Enumerated(EnumType.STRING)
	 private ComunityAttachmentType attachmentType;
	 
	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "userid")
	 @JsonBackReference
	 private Comunity comunity;
	 
	 
	 @Builder
	 public ComunityAttachment(Long id, String originFilename,String storePath, ComunityAttachmentType attachmentType, Comunity comunity) {
		 	this.id = id;
	        this.originFilename = originFilename;
	        this.storeFilename = storePath;
	        this.attachmentType = attachmentType;
	        this.comunity = comunity;
	        
	 }
	 
	 public ComunityAttachment() {
		 comunity = new Comunity();
		  
	 }

	
	 public static ComunityAttachment findByComunityIdAndAttachmentType(List<ComunityAttachment> attachments, Long comunityId, ComunityAttachmentType attachmentType) {
	        return attachments.stream()
	            .filter(attachment -> attachment.getComunity().getId().equals(comunityId) && attachment.getAttachmentType() == attachmentType)
	            .findFirst()
	            .orElse(null);
	    }
	 
	 public void deleteAttachment() {
	        File file = new File(getStoreFilename());
	        if (file.exists()) {
	            file.delete();
	        }
	    }
	 
	 
}