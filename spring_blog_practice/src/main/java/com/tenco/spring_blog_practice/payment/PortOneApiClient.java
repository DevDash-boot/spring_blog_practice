package com.tenco.spring_blog_practice.payment;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Map;

@Slf4j
@Component
@Getter
public class PortOneApiClient {
    @Value("${portone.store-id}")
    private String storeId;
    @Value("${portone.channel-key}")
    private String channelKey;
    @Value("${portone.api-secret}")
    private String apiSecret;

    private static final String PAYMENT_URL = "https://api.portone.io/payments";
    private final RestTemplate restTemplate = createRestTemplate();

    public PaymentResponse.PortOnePayment getPayment(String paymentId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "PortOne " + apiSecret);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<PaymentResponse.PortOnePayment> response = restTemplate.exchange(PAYMENT_URL + "/" + paymentId,
                HttpMethod.GET,
                request,
                PaymentResponse.PortOnePayment.class);
        return response.getBody();
    }
    // 포트원 서버에 결제 취소 요청
    // amount를 넣으면 그 금액만큼 취소됨. 부분 환불이 가능함. amount를 넣지 않으면 전액 취소됨
    // 이번 로직은 결제 금액 전체를 넣는다.
    // 포트원 서버에 결제 취소 요청
    public PaymentResponse.PortOneCancel cancelPayment(String paymentId, int amount) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "PortOne " + apiSecret);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> body = Map.of(
                "amount", amount,
                "reason", "관리자 환불 승인"
        );
        HttpEntity<Map> request = new HttpEntity<>(body, headers);
        ResponseEntity<PaymentResponse.PortOneCancel> response =
                restTemplate.exchange(
                        PAYMENT_URL + "/" + paymentId + "/cancel",
                        HttpMethod.POST,
                        request,
                        PaymentResponse.PortOneCancel.class
                );
        return response.getBody();
    }

    private static RestTemplate createRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(5));   // 서버 연결 대기 최대 5초
        factory.setReadTimeout(Duration.ofSeconds(10));     // 응답 대기 최대 10초(결제 취소는 조금 더 길게)
        return new RestTemplate(factory);
    }
}
