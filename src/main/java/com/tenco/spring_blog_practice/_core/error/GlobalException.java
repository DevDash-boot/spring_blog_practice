package com.tenco.spring_blog_practice._core.error;

// 모든 컨트롤러에서 발생하는 예외 처리

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(Exception400.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handle400(Exception400 e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "err/400";
    }

    @ExceptionHandler(Exception401.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handle401(Exception401 e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "err/401";
    }

    @ExceptionHandler(Exception403.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handle403(Exception403 e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "err/403";
    }

    @ExceptionHandler(Exception404.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle404(Exception404 e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "err/404";
    }

    // 존재하지 않는 정적 리소스 요청(favicon, Chrome DevTools 등) → 로그 없이 404
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> handleNoResource(NoResourceFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handle500(Exception e, Model model) {
        log.error("예상하지 못한 서버 오류", e);
        model.addAttribute("errorMessage", "서버 내부 오류가 발생했습니다.");
        return "err/500";
    }

    // 기타 에러
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public String exRun(RuntimeException e, HttpServletRequest request, Model model) {
        log.error("=== 예상치 못한 에러 발생 ===");
        log.error("요청 URL : {} ", request.getRequestURL());
        log.error("에러 타입 : {} ", e.getClass().getSimpleName());
        log.error("에러 메세지 : {} ", e.getMessage());
        log.error("스택 트레이스 : ", e); // 전체 스택 트레이스 포함

        model.addAttribute("errorMessage", "시스템 오류 발생. 관리자에게 문의해주세요.");

        return "err/500";
    }
}
