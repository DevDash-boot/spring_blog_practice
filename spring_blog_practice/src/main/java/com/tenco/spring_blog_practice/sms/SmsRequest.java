package com.tenco.spring_blog_practice.sms;

import com.tenco.spring_blog_practice._core.error.Exception400;
import lombok.Data;

import java.util.regex.Pattern;

public class SmsRequest {
    // 국내 휴대폰 번호(숫자만) : 010 / 011 / 016 / 017 / 018 / 019 + 7~8자리
    private static final Pattern TEL_PATTERN = Pattern.compile("^01[016789]\\d{7,8}$");

    // 휴대폰 인증 요청 DTO
    @Data
    public static class smsCheckDto {
        private String telNumber;   // 휴대폰 번호(1번째 요청 시 사용)
        private String code;    // 인증 번호(2번째 요청 시 사용)

        // 010-1234-5678, 010 1234 5678 처럼 입력해도 숫자만 남겨서 사용한다.
        public String getNormalizedTelNumber() {
            return telNumber == null ? "" : telNumber.replaceAll("[^0-9]", "");
        }

        public void validate() {
            if (telNumber == null || telNumber.trim().isEmpty()) {
                throw new Exception400("휴대폰 번호를 입력해주세요.");
            }
            if (!TEL_PATTERN.matcher(getNormalizedTelNumber()).matches()) {
                throw new Exception400("올바른 휴대폰 번호 형식이 아닙니다.");
            }
        }

        // 인증 번호 확인. API에서 추가로 호출할 예정
        public void validateCode() {
            validate();
            if (code == null || code.trim().isEmpty()) {
                throw new Exception400("인증 번호를 입력해주세요.");
            }
            if (!code.matches("\\d{6}")) {
                throw new Exception400("인증 번호 6자리를 입력해주세요.");
            }
        }
    }
}
