package com.tenco.spring_blog_practice._core.intercepter;

// Login 여부

import com.tenco.spring_blog_practice._core.error.Exception401;
import com.tenco.spring_blog_practice._core.util.Define;
import com.tenco.spring_blog_practice.user.SessionUserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        SessionUserDto sessionUserDto = (session == null) ? null : (SessionUserDto) session.getAttribute(Define.SESSION_USER);
        if (sessionUserDto == null){
            throw new Exception401("로그인 먼저 해주세요");
        }
        return true;
    }
}
