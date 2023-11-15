package com.example.springsocial.comunity.heart.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springsocial.model.User;
import com.example.springsocial.repository.UserRepository;
import com.example.springsocial.comunity.Comunity;
import com.example.springsocial.comunity.heart.Heart;
import com.example.springsocial.comunity.heart.dto.HeartDto;
import com.example.springsocial.comunity.heart.repository.HeartRepository;
import com.example.springsocial.comunity.repository.ComunityRepository;
import com.example.springsocial.config.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HeartService {
	
	private final ComunityRepository comunityRepository;
	private final  HeartRepository heartRepository;
	private final  UserRepository userRepository;

	public HeartDto allHeart(Long id) {
	    Comunity comunity = comunityRepository.findById(id).orElseThrow(() -> new RuntimeException("글이 없습니다"));
	    List<Heart> hearts = heartRepository.findAllByComunity(comunity);
	    int size = hearts.size();
	    if (size == 0) {
	        return HeartDto.noOne();
	    }

	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication == null || "anonymousUser".equals(authentication.getName())) {
	        return new HeartDto(size, false);
	    } else {
	        // 사용자 ID를 가져오는 방법에 따라 다음 라인을 수정해야 합니다.
	        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();

	        boolean result = hearts.stream().anyMatch(recommend -> recommend.getUser().equals(user));
	        return new HeartDto(size, result);
	    }
	}

	

	@Transactional
	public void createHeart(Long userId, Long comunityId) {
	    User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("사용자 정보가 없습니다"));
	    Comunity comunity = comunityRepository.findById(comunityId).orElseThrow(() -> new RuntimeException("글이 없습니다"));

	    Heart heart = new Heart(user, comunity);
	    heartRepository.save(heart);
	}

	
	@Transactional
	public void removeHeart(Long userId, Long comunityId) {
	    User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
	    Comunity comunity = comunityRepository.findById(comunityId).orElseThrow(() -> new RuntimeException("글이 없습니다"));
	    Heart heart = heartRepository.findByComunityAndUser(comunity, user).orElseThrow(() -> new RuntimeException("추천이 없습니다"));

	    heartRepository.delete(heart);
	}
}


