package com.tenco.spring_blog_practice._core.intercepter;

// 특정 IP 차단

import com.tenco.spring_blog_practice._core.error.Exception403;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class IpBlockInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String blockIp = request.getRemoteAddr();
        if (blockIp.equals("192.168.5.17") ||
                blockIp.equals("192.168.5.18") || blockIp.equals("192.168.7.232"))
            throw new Exception403("차단된 IP로 접근 권한이 없습니다.");
        return true;
    }
}
