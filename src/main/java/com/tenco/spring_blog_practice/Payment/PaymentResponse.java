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
}
