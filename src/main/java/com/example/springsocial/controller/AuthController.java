package com.example.springsocial.controller;

import com.example.springsocial.exception.BadRequestException;
import com.example.springsocial.model.AuthProvider;
import com.example.springsocial.model.User;
import com.example.springsocial.payload.ApiResponse;
import com.example.springsocial.payload.AuthResponse;
import com.example.springsocial.payload.LoginRequest;
import com.example.springsocial.payload.SignUpRequest;
import com.example.springsocial.payload.FindPasswordRequest;
import com.example.springsocial.repository.UserRepository;
import com.example.springsocial.security.TokenProvider;
import com.example.springsocial.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;
    
    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        // Creating user's account
        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setPhone(signUpRequest.getPhone());
        user.setNickname(signUpRequest.getNickname());
        user.setProvider(AuthProvider.local);
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully@"));
    }
    
    @PostMapping("/findpassword")
    public ResponseEntity<?> findPassword(@Valid @RequestBody FindPasswordRequest findPasswordRequest) {
        
        User user = userRepository.findByEmailAndName(findPasswordRequest.getEmail(), findPasswordRequest.getName())
                .orElseThrow(() -> new BadRequestException("User not found with this email and name."));
        
        // 새로운 임시 비밀번호 생성 (예: UUID를 사용)
        String temporaryPassword = UUID.randomUUID().toString();

        // 임시 비밀번호를 암호화하여 저장
        user.setPassword(passwordEncoder.encode(temporaryPassword));
        userRepository.save(user);

        // 임시 비밀번호를 이메일로 전송
        emailService.sendTemporaryPasswordEmail(user.getEmail(), temporaryPassword);

        return ResponseEntity.ok(new ApiResponse(true, "Temporary password sent successfully."));
    }
    
    @GetMapping("/emailcheck/{email}")
    public ResponseEntity<Boolean> checkEmail(@PathVariable String email) {
    	return ResponseEntity.ok(emailService.checkEmail(email));
    }


}
