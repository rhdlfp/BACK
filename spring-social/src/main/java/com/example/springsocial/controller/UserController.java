package com.example.springsocial.controller;

import com.example.springsocial.comunity.Comunity;
import com.example.springsocial.comunity.repository.ComunityRepository;
import com.example.springsocial.exception.ResourceNotFoundException;
import com.example.springsocial.kakaopay.entity.Payment;
import com.example.springsocial.kakaopay.repository.PaymentRepository;
import com.example.springsocial.model.User;
import com.example.springsocial.qna.Qna;
import com.example.springsocial.qna.repository.QnaRepository;
import com.example.springsocial.repository.UserRepository;
import com.example.springsocial.security.CurrentUser;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private QnaRepository qnaRepository;
	@Autowired
	private ComunityRepository comunityRepository;
	@Autowired
	private PaymentRepository paymentRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserService userService;

	@GetMapping("/user/me")
	@PreAuthorize("hasRole('USER')")
	public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
		return userRepository.findById(userPrincipal.getId())
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
	}

	@PutMapping("/user/update/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updateuser) {
		Optional<User> existingUser = userRepository.findById(id);
		if (existingUser.isPresent()) {
			User user = existingUser.get();
			user.setName(updateuser.getName()); // 업데이트할 필드에 대한 설정
			user.setNickname(updateuser.getNickname());
			user.setPhone(updateuser.getPhone());
			user.setEmail(updateuser.getEmail());

			User saveuser = userRepository.save(user);
			return ResponseEntity.status(HttpStatus.OK).body(saveuser);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PutMapping("/user/changePassword/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<User> changePasswordUser(@PathVariable Long id, @RequestBody User changepassword) {
		Optional<User> existingUser = userRepository.findById(id);

		if (existingUser.isPresent()) {
			User user = existingUser.get();

			user.setPassword(passwordEncoder.encode(changepassword.getPassword()));

			User saveuser = userRepository.save(user);
			return ResponseEntity.status(HttpStatus.OK).body(saveuser);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
	
	
	@DeleteMapping("/user/delete")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> deleteUser(@RequestParam Long id) {
	    Optional<User> user = userRepository.findById(id);
	    if (user.isPresent()) {
	        // 사용자가 작성한 글을 찾습니다.
	        List<Qna> userQnas = qnaRepository.findByUserid(user.get().getId());
	        List<Comunity> userComunitys = comunityRepository.findByUserid(user.get().getId());

	        List<Payment> userPayments = paymentRepository.findByUser_Id(user.get().getId()).orElse(Collections.emptyList());
	        paymentRepository.deleteAll(userPayments);
	        
	        // Q&A 글 삭제
	        for (Qna qna : userQnas) {
	            // Q&A 글 삭제
	            qnaRepository.delete(qna);
	        }
	        
	        // Comunity 글 삭제
	        for (Comunity comunity : userComunitys) {
	        	// Q&A 글 삭제
	        	comunityRepository.delete(comunity);
	        }
	        
	        // 사용자 삭제
	        userRepository.deleteById(id);

	        return ResponseEntity.ok("User deleted successfully");
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
	    }
	}


	
	
	
}