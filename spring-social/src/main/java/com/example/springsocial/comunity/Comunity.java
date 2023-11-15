package com.example.springsocial.comunity;

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

import com.fasterxml.jackson.annotation.JsonManagedReference;

import com.example.springsocial.comunity.comment.Comment;
import com.example.springsocial.comunity.heart.Heart;

import lombok.Builder;

@Entity
@Table(name = "comunity")
@DynamicInsert 
@DynamicUpdate 
public class Comunity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
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
	
	@Column(name = "counts")
	private Integer counts = 0;

	@Column(name = "starpoint")
	private Double starpoint = 0.0;
	
	@OneToMany(mappedBy = "comunity", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ComunityAttachment> attachedFiles = new ArrayList<>();
	
    @OneToMany(mappedBy = "comunity", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "comunity", cascade = CascadeType.REMOVE)
    private List<Heart> Hearts = new ArrayList<>();
	
	public List<ComunityAttachment> getAttachedFiles() {
		return attachedFiles;
	}
	public void setAttachedFiles(List<ComunityAttachment> attachedFiles) {
		this.attachedFiles = attachedFiles;
	}
	
	public Comunity() {
		super();
	}
	@Builder
	public Comunity(String type, String title, String contents,
			Long userid, String nickname, Integer memberNo, 
			Date createdTime, Date updatedTime,
			Integer likes, Integer counts, Double starpoint) {
		
		super();
		this.type = type;
		this.title = title;
		this.contents = contents;
		this.userid = userid;
		this.nickname = nickname;
		this.createdTime = createdTime;
		this.updatedTime = updatedTime;
		this.counts = counts;
		this.starpoint = starpoint;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Integer getCounts() {
		return counts;
	}

	public void setCounts(Integer counts) {
		this.counts = counts;
	}
	
	public Double getStarpoint() {
		return starpoint;
	}
	public void setStarpoint(Double starpoint) {
		this.starpoint = starpoint;
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
		return "Coumunity [id=" + id + ", type=" + type + ", title=" + title + ", contents=" + contents + 
				", userid=" + userid + ", nickname=" + nickname + ", createdTime=" + createdTime + 
				", updatedTime=" + updatedTime + ", counts=" + counts + ", starpoint=" + starpoint + "]";
	}


}
