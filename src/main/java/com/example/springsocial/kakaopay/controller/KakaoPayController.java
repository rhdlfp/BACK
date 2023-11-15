package com.example.springsocial.kakaopay.controller;

import com.example.springsocial.kakaopay.exception.BusinessLogicException;
import com.example.springsocial.kakaopay.exception.ExceptionCode;
import com.example.springsocial.kakaopay.repository.BuyListRepository;
import com.example.springsocial.kakaopay.repository.PaymentRepository;
import com.example.springsocial.kakaopay.dto.BuyListDTO;
import com.example.springsocial.kakaopay.dto.KakaoApproveResponse;
import com.example.springsocial.kakaopay.dto.KakaoCancelResponse;
import com.example.springsocial.kakaopay.dto.KakaoReadyResponse;
import com.example.springsocial.kakaopay.dto.PaymentDTO;
import com.example.springsocial.kakaopay.entity.BuyList;
import com.example.springsocial.kakaopay.entity.Payment;
import com.example.springsocial.kakaopay.service.KakaoPayService;
import com.example.springsocial.service.UserService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;
    private final PaymentRepository paymentRepository;
    private final BuyListRepository buyListRepository;
    
    /** 구매내역 1차 리스트 **/
    @GetMapping("/list/{id}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByUserId(@PathVariable Long id) {
        Optional<List<Payment>> optionalPayments = paymentRepository.findByUser_Id(id);

        if (optionalPayments.isPresent()) {
            List<Payment> payments = optionalPayments.get();

            // Payment 엔티티를 PaymentDTO로 변환
            List<PaymentDTO> paymentDTOs = payments.stream()
                    .map(payment -> new PaymentDTO(payment.getId(), payment.getStoreName(), payment.getStoreAddress(), payment.getPrice(), payment.getStatus()))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(paymentDTOs, HttpStatus.OK);
        } else {
            // 사용자 ID에 해당하는 데이터가 없는 경우에 대한 처리
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    /** 구매내역 2차 리스트 **/
    @GetMapping("/buylist/{id}")
    public ResponseEntity<List<BuyListDTO>> getBuyListByPaymentId(@PathVariable Long id) {
        Optional<List<BuyList>> optionalBuyLists = buyListRepository.findByPaymentId(id);

        if (optionalBuyLists.isPresent()) {
            List<BuyList> buyLists = optionalBuyLists.get();

            // BuyList 엔티티를 BuyListDTO로 변환
            List<BuyListDTO> buyListDTO = buyLists.stream()
                    .map(buyList -> new BuyListDTO(buyList.getId(), buyList.getMenuName(), buyList.getQuantity(), buyList.getMenuPrice()))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(buyListDTO, HttpStatus.OK);
        } else {
            // 사용자 ID에 해당하는 데이터가 없는 경우에 대한 처리
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    
    
    /**
     * 결제요청
     */
    @PostMapping("/ready")
    public KakaoReadyResponse readyToKakaoPay(@RequestBody Map<String, Object> request) {
        // 이제 request 맵에서 필요한 값들을 가져와서 사용할 수 있습니다.
        String userId = request.get("id").toString();
        String totalAmount = request.get("total_amount").toString();
        String storeName = request.get("store_name").toString();
        String storeAddress = request.get("store_address").toString();
        List<String> menuNames = (List<String>) request.get("menuNames");
        List<String> quantitys = (List<String>) request.get("quantitys");
        List<String> menuPrices = (List<String>) request.get("menuPrices");
        
        return kakaoPayService.kakaoPayReady(userId, storeName, storeAddress, totalAmount, menuNames, quantitys, menuPrices);
    }
    
    /**
     * 결제 성공
     */
//    @GetMapping("/success")
//    public ResponseEntity afterPayRequest(@RequestParam("pg_token") String pgToken) {
//
//        KakaoApproveResponse kakaoApprove = kakaoPayService.ApproveResponse(pgToken);
//
//        return new ResponseEntity<>(kakaoApprove, HttpStatus.OK);
//    }

    @GetMapping("/success")
    public ResponseEntity<String> afterPayRequest(@RequestParam("pg_token") String pgToken) {
        KakaoApproveResponse kakaoApprove = kakaoPayService.ApproveResponse(pgToken);

        String redirectUrl = "http://localhost:3000/profile"; // 원하는 리액트 앱의 URL로 변경

        // 팝업 닫기 및 리액트 앱으로 리디렉션하는 스크립트
        String script = "<script>window.close(); window.opener.location.href='" + redirectUrl + "';</script>";

        return ResponseEntity.ok(script);
    }
    
    
     /**
     * 결제 진행 중 취소
     */
    @GetMapping("/cancel")
    public void cancel() {

        throw new BusinessLogicException(ExceptionCode.PAY_CANCEL);
    }

    /**
     * 결제 실패
     */
    @GetMapping("/fail")
    public void fail() {

        throw new BusinessLogicException(ExceptionCode.PAY_FAILED);
    }
    
    /**
     * 환불
     */
    @PostMapping("/refund")
    public ResponseEntity refund() {

        KakaoCancelResponse kakaoCancelResponse = kakaoPayService.kakaoCancel();

        return new ResponseEntity<>(kakaoCancelResponse, HttpStatus.OK);
    }
}
