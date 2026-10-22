package com.tenco.spring_blog_practice._core.intercepter;

import com.tenco.spring_blog_practice._core.util.Define;
import com.tenco.spring_blog_practice.user.SessionUserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionUserModleInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 세션이 없으면 새로 만들지 않는다.
        HttpSession session = request.getSession(false);
        if (session != null) {
            SessionUserDto sessionUserDto = (SessionUserDto) session.getAttribute(Define.SESSION_USER);
            if (sessionUserDto != null) {
                // 화면(Mustache)에서 {{#sessionUser}} 로 사용 (에러 페이지 포함)
                request.setAttribute(Define.SESSION_USER, sessionUserDto);
            }
        }
        return true;
    }
}