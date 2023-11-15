package com.example.springsocial.security.oauth2;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.springsocial.exception.OAuth2AuthenticationProcessingException;
import com.example.springsocial.model.AuthProvider;
import com.example.springsocial.model.User;
import com.example.springsocial.repository.UserRepository;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.security.oauth2.user.GithubOAuth2UserInfo;
import com.example.springsocial.security.oauth2.user.GoogleOAuth2UserInfo;
import com.example.springsocial.security.oauth2.user.KakaoOAuth2UserInfo;
import com.example.springsocial.security.oauth2.user.NaverOAuth2UserInfo;
import com.example.springsocial.security.oauth2.user.OAuth2UserInfo;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        System.out.println("getClientRegistration:" + oAuth2UserRequest.getClientRegistration());
		//registrationId로 어떤 OAuth로 로그인했는지 확인가능
		System.out.println("getAccessToken:" + oAuth2UserRequest.getAccessToken().getTokenValue());
		//구글 로그인 버튼 클릭 -> 구글로그인창 -> 로그인 완료 -> code를 리턴 -> Access Token요청
		//userRequest 정보 -> loadUser함수 호출 -> 구글로부터 회원프로필 받아준다
		System.out.println("getAttributes:" + oAuth2User.getAttributes());
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = null;
        		
        if(oAuth2UserRequest.getClientRegistration().getRegistrationId().equals("naver")){
        	System.out.println("네이버 로그인 요청");
			oAuth2UserInfo = new NaverOAuth2UserInfo((Map)oAuth2User.getAttributes().get("response"));
			
        }else if(oAuth2UserRequest.getClientRegistration().getRegistrationId().equals("google")){
        	System.out.println("구글 로그인 요청");
        	oAuth2UserInfo = new GoogleOAuth2UserInfo(oAuth2User.getAttributes());
        }else if(oAuth2UserRequest.getClientRegistration().getRegistrationId().equals("github")){
        	System.out.println("깃허브 로그인 요청");
        	oAuth2UserInfo = new GithubOAuth2UserInfo(oAuth2User.getAttributes());
        }else if(oAuth2UserRequest.getClientRegistration().getRegistrationId().equals("kakao")){
        	System.out.println("카카오 로그인 요청");
        	oAuth2UserInfo = new KakaoOAuth2UserInfo((Map)oAuth2User.getAttributes().get("kakao_account"));
        }
        
        else if (oAuth2UserInfo == null || StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();

        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserRequest.getClientRegistration().getClientId());
        user.setName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        //user.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setName(oAuth2UserInfo.getName());
       //existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }

}
