package com.tenco.spring_blog_practice.sms;

import com.solapi.sdk.SolapiClient;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.service.DefaultMessageService;
import com.tenco.spring_blog_practice._core.error.Exception400;
import com.tenco.spring_blog_practice._core.error.Exception500;
import com.tenco.spring_blog_practice._core.util.Define;
import com.tenco.spring_blog_practice._core.util.MailUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;

@Slf4j
@Service
public class SmsService {
    // application.yaml의 변수를 가지고 올 때 사용
    @Value("${solapi.api-key}")
    private String solapiKey;
    @Value("${solapi.api-secret}")
    private String solapiSecret;
    @Value("${solapi.sender}")
    private String solapiSender;

    private record SmsCode(String code, LocalDateTime expiresAt) implements Serializable {
        boolean isExpired() {
            return LocalDateTime.now().isAfter(expiresAt);
        }
    }

    // 1. 인증 번호 발송
    public void sendVerificationCode(String telNumber, HttpSession session) {
        // 연속 발송 방지(문자 발송 비용 보호)
        long now = System.currentTimeMillis();
        Long lastSent = (Long) session.getAttribute(Define.SMS_LAST_SENT);
        if (lastSent != null && now - lastSent < Define.RESEND_COOLDOWN_SECONDS * 1000L) {
            throw new Exception400("인증 번호는 " + Define.RESEND_COOLDOWN_SECONDS + "초 후에 다시 요청할 수 있습니다.");
        }

        // 6자리 인증 번호 생성
        String code = MailUtil.generateCode();
        DefaultMessageService messageService = SolapiClient.INSTANCE.createInstance(solapiKey, solapiSecret);
        Message message = new Message();
        message.setFrom(solapiSender);
        message.setTo(telNumber);
        message.setText("[스프링 블로그] 인증 번호는 " + code + " 입니다. "
                + Define.CODE_EXPIRE_MINUTES + "분 안에 입력해주세요.");

        try {
            messageService.send(message);
        } catch (Exception e) {
            log.error("문자 발송 실패 - 번호 : {}", telNumber, e);
            throw new Exception500("인증 문자 발송 중 오류가 발생했습니다.");
        }

        // 인증번호와 만료 시간 저장
        session.setAttribute(Define.SMS_LAST_SENT, now);
        session.setAttribute(Define.SMS_CODE_PREFIX + telNumber,
                new SmsCode(code, LocalDateTime.now().plusMinutes(Define.CODE_EXPIRE_MINUTES)));
        session.removeAttribute(Define.SMS_ATTEMPT_PREFIX + telNumber);
        log.info("인증 문자 발송 완료 - 번호 : {}", telNumber);   // 인증 번호 자체는 로그에 남기지 않는다.
    }

    // 2. 인증 번호 확인 : 일치하면 true
    public boolean verifyCode(String telNumber, String code, HttpSession session) {
        String key = Define.SMS_CODE_PREFIX + telNumber;
        String attemptKey = Define.SMS_ATTEMPT_PREFIX + telNumber;

        SmsCode savedSmsCode = (SmsCode) session.getAttribute(key);
        if (savedSmsCode == null) {
            throw new Exception400("인증 번호를 먼저 요청해주세요.");
        }
        if (savedSmsCode.isExpired()) {
            session.removeAttribute(key);   // 만료된 인증 번호 삭제
            session.removeAttribute(attemptKey);
            throw new Exception400("인증 번호가 만료되었습니다. 다시 요청해 주세요.");
        }

        // 시도 횟수 증가 후 초과하면 인증 번호 폐기(무작위 대입 방지)
        Integer prev = (Integer) session.getAttribute(attemptKey);
        int attempts = (prev == null) ? 1 : prev + 1;
        if (attempts > Define.MAX_VERIFY_ATTEMPTS) {
            session.removeAttribute(key);
            session.removeAttribute(attemptKey);
            throw new Exception400("시도 횟수를 초과했습니다. 인증 번호를 다시 요청해 주세요.");
        }

        if (!savedSmsCode.code().equals(code)) {
            session.setAttribute(attemptKey, attempts);
            throw new Exception400("인증 번호가 일치하지 않습니다.");
        }

        // 성공 시 사용한 인증 번호는 삭제(한 번만 사용할 수 있게)
        session.removeAttribute(key);
        session.removeAttribute(attemptKey);
        // 인증을 완료한 휴대폰 번호를 세션에 기록
        session.setAttribute(Define.VERIFIED_TEL_NUMBER, telNumber);
        return true;
    }
}
