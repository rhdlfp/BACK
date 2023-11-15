package com.example.springsocial.comunity.heart.dto;

public class DeleteHeartDto {
	private Long userId;
	private Long comunityId;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getComunityId() {
		return comunityId;
	}
	public void setComunityId(Long comunityId) {
		this.comunityId = comunityId;
	}
}
