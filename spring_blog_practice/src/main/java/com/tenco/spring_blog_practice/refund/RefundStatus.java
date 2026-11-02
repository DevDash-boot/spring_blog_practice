package com.tenco.spring_blog_practice.refund;

public enum RefundStatus {
    PENDING("대기중"),
    APPROVED("승인됨"),
    REJECTED("거절됨");

    private final String displayName;

    RefundStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // 요청 파라미터(PENDING, APPROVED, REJECTED)를 enum으로 변환 - 값이 없거나 잘못되면 null(전체 조회)
    public static RefundStatus from(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return RefundStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}