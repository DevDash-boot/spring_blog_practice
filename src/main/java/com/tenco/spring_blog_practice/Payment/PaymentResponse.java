package com.tenco.spring_blog_practice.Payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public class PaymentResponse {
    @Data
    public static class PrepareDto {
        private String paymentId;
        private int amount;
        private String storeId;
        private String channelKey;

        @Builder
        public PrepareDto(String paymentId, int amount, String storeId, String channelKey) {
            this.paymentId = paymentId;
            this.amount = amount;
            this.storeId = storeId;
            this.channelKey = channelKey;
        }
    }

    @Data
    @AllArgsConstructor
    public static class CompleteDto {
        private int amount;
        private int currentPoint;
    }

    @Data
    public static class PortOnePayment {
        private String id;
        private String status;
        private String pgTxId;
        private Amount amount;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Amount {
            private Integer total;
        }
    }

    @Data
    public static class ListDto {
        private Long id;
        private String pgTxId;
        private String paymentId;
        private String paymentAt;
        private String statusDisplay;
        private String statusBadgeClass;
        private String amount;
        private boolean cancelled;  // 환불 완료 결제 인지 확인
        private boolean refundable;     // 환불 가능한지

        @Builder
        public ListDto(Payment payment) {
            this.id = payment.getId();
            this.pgTxId = payment.getPgTxId();
            this.paymentId = payment.getPaymentId();
            this.paymentAt = payment.getCreatedAt().toString();
            this.statusDisplay = payment.getStatus().getDisplayName();
            this.statusBadgeClass = payment.getStatus().getBadgeClass();
            this.amount = String.valueOf(payment.getAmount());
            this.cancelled = payment.getStatus() == PaymentStatus.CANCELLED;
            this.refundable = refundable;
        }
    }
}
