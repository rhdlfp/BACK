package com.example.springsocial.kakaopay.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.springsocial.kakaopay.dto.KakaoApproveResponse;
import com.example.springsocial.kakaopay.dto.KakaoCancelResponse;
import com.example.springsocial.kakaopay.dto.KakaoReadyResponse;
import com.example.springsocial.kakaopay.entity.BuyList;
import com.example.springsocial.kakaopay.entity.Payment;
import com.example.springsocial.kakaopay.repository.BuyListRepository;
import com.example.springsocial.kakaopay.repository.PaymentRepository;
import com.example.springsocial.model.User;
import com.example.springsocial.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoPayService {

    static final String cid = "TC0ONETIME"; // 가맹점 테스트 코드
    static final String admin_Key = "32b24c300bb922e72b7ef3b26e6055b7"; // 공개 조심! 본인 애플리케이션의 어드민 키를 넣어주세요
    private KakaoReadyResponse kakaoReady;
    private final PaymentRepository paymentRepository;
    private final BuyListRepository buyListRepository;
    private final UserService userService;
    
    
    
    
    public KakaoReadyResponse kakaoPayReady(String userId, String storeName, String storeAddress, String totalAmount, List<String> menuNames, List<String> quantitys, List<String> menuPrices) {
    	
        // 카카오페이 요청 양식
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("partner_order_id", "가맹점 주문 번호"); // 가맹점 주문 번호
        parameters.add("partner_user_id", "가맹점 회원 ID");
        parameters.add("item_name", storeName);
        parameters.add("quantity", "1");
        parameters.add("total_amount", totalAmount);
        parameters.add("vat_amount", "200");
        parameters.add("tax_free_amount", "0");
        parameters.add("approval_url", "http://localhost:8080/payment/success"); // 성공 시 redirect url
        parameters.add("cancel_url", "http://localhost:8080/payment/cancel"); // 취소 시 redirect url
        parameters.add("fail_url", "http://localhost:8080/payment/fail"); // 실패 시 redirect url

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

        // 외부에 보낼 URL
        RestTemplate restTemplate = new RestTemplate();

        kakaoReady = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/ready",
                requestEntity,
                KakaoReadyResponse.class);
    	
    	
        // Payment 엔티티를 먼저 저장
        Payment payment = new Payment();
        payment.setTid(kakaoReady.getTid());
        payment.setStoreName(storeName);
        payment.setStoreAddress(storeAddress);
        payment.setPrice(totalAmount);
        payment.setStatus("waiting");

        User user = userService.getUserById(Long.parseLong(userId));
        payment.setUser(user);

        Payment savedPayment = paymentRepository.save(payment); // 데이터베이스에 저장한 Payment 엔티티를 다시 가져옴

        if (menuNames != null && menuPrices != null && menuNames.size() == menuPrices.size()) {
            List<BuyList> buyLists = new ArrayList<>();
            for (int i = 0; i < menuNames.size(); i++) {
                BuyList buyList = new BuyList();
                buyList.setMenuName(menuNames.get(i));
                buyList.setMenuPrice(menuPrices.get(i));
                buyList.setQuantity(quantitys.get(i));
                buyList.setPayment(savedPayment);
                buyLists.add(buyList);
            }

            // 데이터베이스에 BuyList 엔티티 저장
            buyListRepository.saveAll(buyLists);
        } else {
            // 목록의 크기가 일치하지 않을 때 예외 처리 또는 에러 처리를 수행하십시오.
        }

        return kakaoReady;
    }



    
    
    /**
     * 결제 완료 승인
     */
    public KakaoApproveResponse ApproveResponse(String pgToken) {
    
        // 카카오 요청
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", kakaoReady.getTid());
        parameters.add("partner_order_id", "가맹점 주문 번호");
        parameters.add("partner_user_id", "가맹점 회원 ID");
        parameters.add("pg_token", pgToken);

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
        
        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();
        
        KakaoApproveResponse approveResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/approve",
                requestEntity,
                KakaoApproveResponse.class);
        
        String tid = approveResponse.getTid();
        Optional<Payment> optionalPayment = paymentRepository.findByTid(tid);

        if (optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();
            payment.setStatus("completed");
            paymentRepository.save(payment);
        }

        
        return approveResponse;
    }
    
    
    /**
    * 결제 환불
    */
    public KakaoCancelResponse kakaoCancel() {

        // 카카오페이 요청
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", "환불할 결제 고유 번호");
        parameters.add("cancel_amount", "환불 금액");
        parameters.add("cancel_tax_free_amount", "환불 비과세 금액");
        parameters.add("cancel_vat_amount", "환불 부가세");

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
    
        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();
    
        KakaoCancelResponse cancelResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/cancel",
                requestEntity,
                KakaoCancelResponse.class);
                
        return cancelResponse;
    }
    
    
    /**
     * 카카오 요구 헤더값
     */
    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();

        String auth = "KakaoAK " + admin_Key;

        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        return httpHeaders;
    }
}