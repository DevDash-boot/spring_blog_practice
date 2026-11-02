package com.tenco.spring_blog_practice.refund;

import com.tenco.spring_blog_practice._core.util.MyDateUtil;
import lombok.Builder;
import lombok.Data;

public class RefundResponse {
    @Data
    public static class ListDto {
        private Long id;    // 환불 요청 PK
        private int amount;
        private String paymentId;
        private String reason;
        private String rejectReason;
        private String statusDisplay;
        private String requestedAt;
        private boolean approved;
        private boolean pending;
        private boolean rejected;

        @Builder
        public ListDto(Refund refund) {
            this.id = refund.getId();
            this.amount = refund.getPayment().getAmount();
            this.paymentId = refund.getPayment().getPaymentId();
            this.reason = refund.getReason();
            if (refund.getCreatedAt() != null) {
                this.requestedAt = MyDateUtil.timestampFormat(refund.getCreatedAt());
            }
            this.statusDisplay = refund.getStatus().getDisplayName();
            this.rejectReason = refund.getRejectReason() != null ? refund.getRejectReason() : "";

            this.approved = refund.getStatus() == RefundStatus.APPROVED;
            this.pending = refund.getStatus() == RefundStatus.PENDING;
            this.rejected = refund.getStatus() == RefundStatus.REJECTED;
        }
    }

    @Data
    public static class AdminListDto {
        private Long id;    // 환불 요청 pk
        private String username;    // 환불 신청자
        private String paymentId;   // 포트원 결제건 별 식별자
        private int amount;     // 환불 금액
        private String reason;  // 신청자 환불 사유
        private String rejectReason;    // 관리자 거절 사유(거절된 경우에만 값이 있음)
        private String statusDisplay;   // 대기, 승인, 거절
        private String requestedAt; // 환불 요청 일
        private boolean pending;
        private boolean approved;
        private boolean rejected;

        @Builder
        public AdminListDto(Refund refund) {
            this.id = refund.getId();
            this.username = refund.getUser().getUsername();
            this.paymentId = refund.getPayment().getPaymentId();
            this.amount = refund.getPayment().getAmount();
            this.reason = refund.getReason();
            this.rejectReason = refund.getRejectReason() != null ? refund.getRejectReason() : "";
            this.statusDisplay = refund.getStatus().getDisplayName();
            if (refund.getCreatedAt() != null) {
                this.requestedAt = MyDateUtil.timestampFormat(refund.getCreatedAt());
            }
            this.approved = refund.getStatus() == RefundStatus.APPROVED;
            this.pending = refund.getStatus() == RefundStatus.PENDING;
            this.rejected = refund.getStatus() == RefundStatus.REJECTED;
        }
    }

    @Data
    public static class ApproveDto {
        private String paymentId;   // 포트원 결제 건 식별자(경로로 활용됨)
        private int amount; // DB 조회해야 알 수 있다.

        @Builder
        public ApproveDto(String paymentId, int amount) {
            this.paymentId = paymentId;
            this.amount = amount;
        }
    }

    // 관리자 대시보드 - 상태별 환불 요청 건수
    @Data
    public static class StatusCountDto {
        private long pending;
        private long approved;
        private long rejected;
        private long total;

        public StatusCountDto(long pending, long approved, long rejected) {
            this.pending = pending;
            this.approved = approved;
            this.rejected = rejected;
            this.total = pending + approved + rejected;
        }
    }
}
