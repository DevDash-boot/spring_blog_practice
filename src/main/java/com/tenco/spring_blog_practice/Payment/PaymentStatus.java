package com.tenco.spring_blog_practice.Payment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
    READY("결제 대기", "bg-primary"),
    PAID("결제 완료", "bg-success"),
    CANCELLED("결제 취소", "bg-warning text-dark"),
    REFUND("환불", "bg-danger"),
    FAILED("결제 실패", "bg-secondary");

    private final String displayName;
    private final String badgeClass;
}
