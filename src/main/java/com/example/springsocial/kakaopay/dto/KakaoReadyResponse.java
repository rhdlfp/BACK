package com.example.springsocial.kakaopay.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* 
* 결제 요청 시 카카오에게 받음
*/
@Getter
@Setter
@ToString
public class KakaoReadyResponse {

    private String tid; // 결제 고유 번호
    private String next_redirect_pc_url; // pc 웹일 경우 받는 결제 페이지
    private String created_at;
}