package com.example.springsocial.comunity.comment.dto;

import java.sql.Timestamp;

import com.example.springsocial.comunity.comment.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponseDto {
    private long commentId;
    private long userId;
    private String userNickname;
    private String commentBody;
    private Long createdAt;
    //private boolean isWritten;

    public static CommentResponseDto of(Comment comment, boolean bool) {
        return CommentResponseDto.builder()
                .commentId(comment.getId())
                .userId(comment.getUser().getId())
                .userNickname(comment.getUser().getNickname())
                .commentBody(comment.getText())
                .createdAt(Timestamp.valueOf(comment.getCreatedAt()).getTime())
                //.isWritten(bool)
                .build();
    }
}