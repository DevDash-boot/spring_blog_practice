package com.tenco.spring_blog_practice.Payment;

import com.tenco.spring_blog_practice._core.error.Exception400;
import com.tenco.spring_blog_practice._core.error.Exception403;
import com.tenco.spring_blog_practice.user.User;
import com.tenco.spring_blog_practice.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {
    private final PaymentJpaRepository paymentJpaRepository;
    private final UserJpaRepository userJpaRepository;

    private String generatePaymentId(Long userId) {
        return "point_" + userId + "_" + System.currentTimeMillis()
                + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    @Transactional
    public String prepare(Long sessionUserId, Integer amount) {
        User user = userJpaRepository.findById(sessionUserId)
                .orElseThrow(() -> new Exception400("사용자를 찾을 수 없습니다."));
        String paymentId = generatePaymentId(sessionUserId);
        while (paymentJpaRepository.existsByPaymentId(paymentId)) {
            paymentId = generatePaymentId(sessionUserId);
        }
        Payment payment = Payment.builder().paymentId(paymentId).user(user).amount(amount).build();
        paymentJpaRepository.save(payment);
        return paymentId;
    }

    @Transactional
    public Object complete(Long sessionUserId, String paymentId, PaymentResponse.PortOnePayment portOnePayment) {
        // 1. 우리 서버가 발급한 결제 건인지 확인
        Payment payment = paymentJpaRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new Exception400("결제 정보를 찾을 수 없습니다."));
        // 2. 결제를 진행하는 본인인지 확인
        if (!payment.isOwner(sessionUserId)) {
            throw new Exception403("본인의 결제만 처리할 수 있습니다.");
        }
        // 3. 중복 지급 방지 : 이미 PAID이면 거부
        if (payment.isPaid()) {
            throw new Exception400("이미 처리된 결제입니다.");
        }
        // 4. 포트원 기준 결제 상태 확인
        if (!"PAID".equals(portOnePayment.getStatus())) {
            throw new Exception400("결제가 완료되지 않았습니다. " + portOnePayment.getStatus());
        }
        // 5. 금액 위 변조 확인 : 실제 결제 금액 = 1번에서 기록한 금액이 같아야함
        if (portOnePayment.getAmount() == null ||
                portOnePayment.getAmount().getTotal() == null ||
                portOnePayment.getAmount().getTotal() != payment.getAmount()) {
            throw new Exception400("결제 금액이 일치하지 않습니다.");
        }
        // 6. 검증 통과 : 포인트 충전 + 결제 완료 표시
        User user = payment.getUser();
        user.chargePoint(payment.getAmount());  // 해당 유저의 포인트 충전됨
        payment.markPaid(portOnePayment.getPgTxId());
        log.info("결제 완료 및 포인트 충전 - 사용자 ID : {}, paymentId : {}, 금액 : {}",
                sessionUserId, paymentId, payment.getAmount());
        return new PaymentResponse.CompleteDto(payment.getAmount(), user.getPoint());
    }
}
