package com.example.springsocial.comunity.comment.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springsocial.comunity.Comunity;
import com.example.springsocial.comunity.comment.Comment;
import com.example.springsocial.comunity.comment.dto.CommentResponseDto;
import com.example.springsocial.comunity.comment.repository.CommentRepository;
import com.example.springsocial.comunity.repository.ComunityRepository;
import com.example.springsocial.model.User;
import com.example.springsocial.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final ComunityRepository comunityRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public List<CommentResponseDto> getComment(Long id) {
    	Comunity comunity = comunityRepository.findById(id).orElseThrow(() -> new RuntimeException("댓글이 없습니다."));
        List<Comment> comments = commentRepository.findAllByComunity(comunity);
        if (comments.isEmpty()) {
            return Collections.emptyList();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == "anonymousUser") {
            return comments
                    .stream()
                    .map(comment -> CommentResponseDto.of(comment, false))
                    .collect(Collectors.toList());
        } else {
            User user = userRepository.findById(Long.parseLong(authentication.getName())).orElseThrow();
            Map<Boolean, List<Comment>> collect = comments.stream()
                    .collect(
                            Collectors.partitioningBy(
                                    comment -> comment.getUser().equals(user)
                            )
                    );
            List<CommentResponseDto> tCollect = collect.get(true).stream()
                    .map(t -> CommentResponseDto.of(t, true))
                    .collect(Collectors.toList());
            List<CommentResponseDto> fCollect = collect.get(false).stream()
                    .map(f -> CommentResponseDto.of(f, false))
                    .collect(Collectors.toList());

            return Stream
                    .concat(tCollect.stream() ,fCollect.stream())
                    .sorted(Comparator.comparing(CommentResponseDto::getCommentId))
                    .collect(Collectors.toList());

        }

    }

    @Transactional
    public CommentResponseDto createComment(Long userId, Long id, String text) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
        Comunity comunity = comunityRepository.findById(id).orElseThrow(() -> new RuntimeException("댓글이 없습니다."));

        Comment comment = Comment.builder()
                .text(text)
                .comunity(comunity)
                .user(user)
                .build();

        return CommentResponseDto.of(commentRepository.save(comment), true);

    }

    @Transactional
    public void removeComment(Long userId, Long id) {
    	User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("로그인 하십시오"));
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("댓글이 없습니다."));
        if (!comment.getUser().equals(user)) {
            throw new RuntimeException("작성자와 로그인이 일치하지 않습니다.");
        }
        commentRepository.delete(comment);
    }
}
