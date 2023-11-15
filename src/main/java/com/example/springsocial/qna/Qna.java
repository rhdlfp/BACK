package com.example.springsocial.qna;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Builder;

@Entity
@Table(name = "qna")
@DynamicInsert 
@DynamicUpdate 
public class Qna {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long no;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "contents")
	private String contents;
	
	@Column(name = "userid")
	private Long userid;

	@Column(name = "nickname")
	private String nickname;
		
	@Column(name = "created_time")
	private Date createdTime;
	
	@Column(name = "updated_time")
	private Date updatedTime;
	
    @OneToMany(mappedBy = "qna", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Attachment> attachedFiles = new ArrayList<>();
	
	public List<Attachment> getAttachedFiles() {
		return attachedFiles;
	}

	public void setAttachedFiles(List<Attachment> attachedFiles) {
		this.attachedFiles = attachedFiles;
	}

	public Qna() {
		super();
	}
	
	@Builder
	public Qna(String title, String type, 
			String contents, Long userid, String nickname,
			Date createdTime, Date updatedTime) {
		
		super();
		this.type = type;
		this.title = title;
		this.contents = contents;
		this.userid = userid;
		this.nickname = nickname;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
	}

	
	public Long getNo() {
		return no;
	}

	public void setNo(Long no) {
		this.no = no;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}
	
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	

	@PrePersist
	protected void onCreate() {
	    this.createdTime = new Date();
	    setKoreanTime(this.createdTime);
	}

	@PreUpdate
	protected void onUpdate() {
	    this.updatedTime = new Date();
	    setKoreanTime(this.updatedTime);
	}

	private void setKoreanTime(Date time) {
	    ZoneId utcZone = ZoneId.of("UTC");
	    ZoneId seoulZone = ZoneId.of("Asia/Seoul");

	    Instant instant = time.toInstant();
	    LocalDateTime seoulTime = instant.atZone(utcZone).withZoneSameInstant(seoulZone).toLocalDateTime();
	    this.updatedTime = Date.from(seoulTime.atZone(seoulZone).toInstant());
	}
	
	@Override
	public String toString() {
		return "qna [no=" + no + ", type=" + type + ", userid=" + userid + 
				",nickname=" + nickname + ", title=" + title + ", contents=" + contents + 
				", createdTime=" + createdTime + ", updatedTime=" + updatedTime +"]";
	}




}
