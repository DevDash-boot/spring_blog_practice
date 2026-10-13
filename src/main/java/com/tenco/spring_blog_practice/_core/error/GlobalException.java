package com.tenco.spring_blog_practice._core.error;

// 모든 컨트롤러에서 발생하는 예외 처리

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalException {
    // 400
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(Exception400.class)
    public String ex400(Exception400 e, HttpServletRequest request) {
        log.warn("=== 400 Bad Request 에러 발생 ===");
        log.warn("요청 URL : {}", request.getRequestURL());
        log.warn("에러 메시지 : {}", e.getMessage());
        log.warn("예외 클래스 : {}", e.getClass().getSimpleName());
        String msg = e.getMessage().replace("'", "\\");    // 따옴표 깨짐 방지

        return """
                <script>
                    alert('%s');
                    history.back();
                </script>
                """.formatted(msg);
    }

    // 401
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    @ExceptionHandler(Exception401.class)
    public String ex401(Exception401 e, HttpServletRequest request) {
        log.warn("=== 401 Unauthorized 에러 발생 ===");
        log.warn("요청 URL : {}", request.getRequestURL());
        log.warn("에러 메시지 : {}", e.getMessage());
        log.warn("예외 클래스 : {}", e.getClass().getSimpleName());
        String msg = e.getMessage().replace("'", "\\");    // 따옴표 깨짐 방지

        return """
                <script>
                    alert('%s');
                    history.back();
                </script>
                """.formatted(msg);
    }

    // 403
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler(Exception403.class)
    public String ex403(Exception403 e, HttpServletRequest request) {
        log.warn("=== 403 Forbidden 에러 발생 ===");
        log.warn("요청 URL : {}", request.getRequestURL());
        log.warn("에러 메시지 : {}", e.getMessage());
        log.warn("예외 클래스 : {}", e.getClass().getSimpleName());
        String msg = e.getMessage().replace("'", "\\");    // 따옴표 깨짐 방지

        return """
                <script>
                    alert('%s');
                    history.back();
                </script>
                """.formatted(msg);
    }

    // 404
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(Exception404.class)
    public String ex404(Exception404 e, HttpServletRequest request) {
        log.warn("=== 404 Not Found 에러 발생 ===");
        log.warn("요청 URL : {}", request.getRequestURL());
        log.warn("에러 메시지 : {}", e.getMessage());
        log.warn("예외 클래스 : {}", e.getClass().getSimpleName());
        String msg = e.getMessage().replace("'", "\\");    // 따옴표 깨짐 방지

        return """
                <script>
                    alert('%s');
                    history.back();
                </script>
                """.formatted(msg);
    }

    // 500
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @ExceptionHandler(Exception500.class)
    public String ex500(Exception500 e, HttpServletRequest request) {
        log.warn("=== 500 Internal Server Error 에러 발생 ===");
        log.warn("요청 URL : {}", request.getRequestURL());
        log.warn("에러 메시지 : {}", e.getMessage());
        log.warn("스택 트레이스 : ", e);
        String msg = e.getMessage().replace("'", "\\");    // 따옴표 깨짐 방지

        return """
                <script>
                    alert('%s');
                    history.back();
                </script>
                """.formatted(msg);
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
