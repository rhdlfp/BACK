package com.example.springsocial.security.oauth2.user;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("id");
    }

//    @Override
//    public String getName() {
//        return "kakao";
//    }
    @Override
    public String getName() {
        if(attributes.containsKey("profile")) {
            Map<String, Object> profile = (Map<String, Object>) attributes.get("profile");
            if(profile.containsKey("nickname")) {
                return (String) profile.get("nickname");
            }
        }
        return "kakao";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("email");
    }
}
