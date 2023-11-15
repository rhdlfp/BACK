package com.example.springsocial.comunity.comment.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
	private Long userId;
    private Long comunityId;
    private String body;
}