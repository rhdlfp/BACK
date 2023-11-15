package com.example.springsocial.comunity.comment.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springsocial.comunity.comment.dto.CommentRequestDto;
import com.example.springsocial.comunity.comment.dto.CommentResponseDto;
import com.example.springsocial.comunity.comment.service.CommentService;
import com.example.springsocial.comunity.comment.dto.MessageDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
	private final CommentService commentService;

    @GetMapping("/list")
    public ResponseEntity<List<CommentResponseDto>> getComments(@RequestParam Long id) {
        return ResponseEntity.ok(commentService.getComment(id));
    }

    @PostMapping("/add")
    public ResponseEntity<CommentResponseDto> postComment(@RequestBody CommentRequestDto request) {
        return ResponseEntity.ok(commentService.createComment(request.getUserId() ,request.getComunityId(), request.getBody()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<MessageDto> deleteComment(@RequestParam Long userId, @RequestParam Long id) {
        commentService.removeComment(userId, id);
        return ResponseEntity.ok(new MessageDto("Success"));
    }
}
