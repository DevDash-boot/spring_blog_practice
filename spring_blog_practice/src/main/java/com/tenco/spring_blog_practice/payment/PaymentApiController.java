package com.tenco.spring_blog_practice.payment;

import com.tenco.spring_blog_practice._core.error.Exception400;
import com.tenco.spring_blog_practice._core.error.Exception403;
import com.tenco.spring_blog_practice._core.util.Define;
import com.tenco.spring_blog_practice.user.SessionUserDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PaymentApiController {
    private final PortOnePaymentService portOnePaymentService;

    @PostMapping("/api/payment/prepare")
    public ResponseEntity<?> prepare(@RequestBody PaymentRequest.PrepareDto prepareDto, HttpSession session) {
        SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute(Define.SESSION_USER);
        if (sessionUserDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인이 필요합니다."));
        }
        prepareDto.validate();

        return ResponseEntity.ok(portOnePaymentService.prepare(sessionUserDto.getId(), prepareDto.getAmount()));
    }

    @PostMapping("/api/payment/complete")
    public ResponseEntity<?> complete(@RequestBody PaymentRequest.CompleteDto completeDto, HttpSession session) {
        SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute(Define.SESSION_USER);
        if (sessionUserDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인이 필요합니다."));
        }
        completeDto.validate();
        return ResponseEntity.ok(portOnePaymentService.complete(sessionUserDto.getId(), completeDto.getPaymentId()));
    }

    // 이 컨트롤러에서 발생한 예외는 화면(HTML)이 아니라 JSON으로 응답한다.
    // (자바스크립트의 response.json()이 정상적으로 동작해야 안내 메시지를 보여줄 수 있다.)
    @ExceptionHandler(Exception400.class)
    public ResponseEntity<Map<String, String>> handleEx400(Exception400 e) {
        return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(Exception403.class)
    public ResponseEntity<Map<String, String>> handleEx403(Exception403 e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleEx500(RuntimeException e) {
        log.error("결제 API 처리 중 오류", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "결제 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요."));
    }
}
