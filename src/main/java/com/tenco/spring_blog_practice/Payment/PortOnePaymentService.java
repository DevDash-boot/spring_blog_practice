package com.tenco.spring_blog_practice.Payment;

import com.tenco.spring_blog_practice._core.error.Exception400;
import com.tenco.spring_blog_practice._core.error.Exception500;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

@Service
@RequiredArgsConstructor
public class PortOnePaymentService {
    private final PortOneApiClient portOneApiClient;
    private final PaymentService paymentService;

    public Object prepare(Long sessionUserId, Integer amount) {
        String paymentId = paymentService.prepare(sessionUserId, amount);

        return PaymentResponse.PrepareDto.builder()
                .paymentId(paymentId)
                .amount(amount)
                .storeId(portOneApiClient.getStoreId())
                .channelKey(portOneApiClient.getChannelKey())
                .build();
    }

    public Object complete(Long sessionUserId, String paymentId) {
        PaymentResponse.PortOnePayment portOnePayment;
        try {
            portOnePayment = portOneApiClient.getPayment(paymentId);
        } catch (HttpClientErrorException e) {
            throw new Exception400("결제 서버에 연결할 수 없습니다.");
        } catch (ResourceAccessException e) {
            throw new Exception500("결제 서버에 연결할 수 없습니다.");
        }
        if (portOnePayment == null) {
            throw new Exception400("결제 정보를 확인할 수 없습니다.");
        }
        return paymentService.complete(sessionUserId, paymentId, portOnePayment);
    }
}
