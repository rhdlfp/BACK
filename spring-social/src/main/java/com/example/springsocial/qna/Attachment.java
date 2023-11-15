package com.example.springsocial.qna;


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
public class Attachment {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id; 
	 private String originFilename;
	 private String storeFilename;
	 
	 @Enumerated(EnumType.STRING)
	 private AttachmentType attachmentType;
	 
	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "userid")
	 @JsonBackReference
	 private Qna qna;
	 
	 
	 @Builder
	 public Attachment(Long id, String originFilename,String storePath, AttachmentType attachmentType, Qna qna) {
		 	this.id = id;
	        this.originFilename = originFilename;
	        this.storeFilename = storePath;
	        this.attachmentType = attachmentType;
	        this.qna = qna;
	        
	 }
	 
	 public Attachment() {
		  qna = new Qna();
		  
	 }

	
	 
	 
	 
}
