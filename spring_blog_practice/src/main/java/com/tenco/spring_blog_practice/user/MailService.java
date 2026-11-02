package com.tenco.spring_blog_practice.user;

import com.tenco.spring_blog_practice._core.error.Exception400;
import com.tenco.spring_blog_practice._core.error.Exception500;
import com.tenco.spring_blog_practice._core.util.Define;
import com.tenco.spring_blog_practice._core.util.MailUtil;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    // 메일 발송. 인증 번호 확인

    // 의존성 설정. application.yaml의 spring.mail 설정으로 스프링 부트가 만들어준 빈 객체
    private final JavaMailSender javaMailSender;

    // record 문법 활용 : JDK 16버전 부터 사용.
    // 아래 코드가 클래스를 설계해 준다.
    // 필드 + getter + equals + hashCode + toString을 가진 것을 자바가 대신 만들어준다.(setter 없음)
    private record EmailCode(String code, LocalDateTime expiresAt) {
        boolean isExpired() {
            return LocalDateTime.now().isAfter(expiresAt);
        }
    }

    // 1. 인증 번호 발송
    public void sendVerificationCode(String email, HttpSession session) {
        long now = System.currentTimeMillis();
        Long lastSent = (Long) session.getAttribute(Define.EMAIL_LAST_SENT);
        if (lastSent != null && now - lastSent < Define.RESEND_COOLDOWN_SECONDS * 1000L) {
            throw new Exception400("인증 번호는 " + Define.RESEND_COOLDOWN_SECONDS + "초 후에 다시 요청할 수 있습니다.");
        }

        // 1-1. 6자리 인증 번호 생성
        String code = MailUtil.generateCode();
        // 1-2. 메일 작성 (MimeMessage : HTML 본문을 만들 수 있고, 첨부 파일 등을 담을 수 있는 메일 형식)
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            // false : 첨부 파일 없음. "UTF-8" : 한글 깨짐 방지
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setTo(email);    // 받는 사람 이메일 주소
            helper.setSubject("[스프링 블로그] 회원가입 인증 번호");       // 제목
            helper.setText("<h3>인증 번호는 " + code + " 입니다.</h3>" +
                    "<p>" + Define.CODE_EXPIRE_MINUTES + "분 안에 입력해주세요.</p>", true);  // 본문
        } catch (Exception e) {
            throw new Exception500("메일 작성 중 오류가 발생했습니다.");
        }
        // 1-3. 인증 번호 발송
        javaMailSender.send(message);
        session.setAttribute(Define.EMAIL_LAST_SENT, now);

        session.setAttribute(Define.EMAIL_CODE_PREFIX + email,
                new EmailCode(code, LocalDateTime.now().plusMinutes(Define.CODE_EXPIRE_MINUTES)));
        session.removeAttribute(Define.EMAIL_ATTEMPT_PREFIX + email);
        log.info("인증 번호 발송 완료 - 이메일 : {}", email);   // 인증 번호 자체는 로그에 남기지 않는다.
    }

    // 2. 인증 번호 확인 : 일치하면 true
    public boolean verifyCode(String email, String code, HttpSession session) {
        String key = Define.EMAIL_CODE_PREFIX + email;
        String attemptKey = Define.EMAIL_ATTEMPT_PREFIX + email;

        EmailCode savedEmailCode = (EmailCode) session.getAttribute(key);
        if (savedEmailCode == null) {
            throw new Exception400("인증 번호를 먼저 요청해주세요.");
        }
        if (savedEmailCode.isExpired()) {
            session.removeAttribute(key);
            session.removeAttribute(attemptKey);
            throw new Exception400("인증 번호가 만료되었습니다. 다시 요청해 주세요.");
        }

        // 시도 횟수 증가 후 초과하면 인증 번호 폐기
        Integer prev = (Integer) session.getAttribute(attemptKey);
        int attempts = (prev == null) ? 1 : prev + 1;
        if (attempts > Define.MAX_VERIFY_ATTEMPTS) {
            session.removeAttribute(key);
            session.removeAttribute(attemptKey);
            throw new Exception400("시도 횟수를 초과했습니다. 인증 번호를 다시 요청해 주세요.");
        }

        if (!savedEmailCode.code().equals(code)) {
            session.setAttribute(attemptKey, attempts);
            throw new Exception400("인증 번호가 일치하지 않습니다.");
        }

        session.removeAttribute(key);
        session.removeAttribute(attemptKey);
        session.setAttribute(Define.VERIFIED_EMAIL, email);
        return true;
    }
}
