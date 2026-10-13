package com.tenco.spring_blog_practice._core.config;

import com.tenco.spring_blog_practice._core.intercepter.IpBlockInterceptor;
import com.tenco.spring_blog_practice._core.intercepter.LoginInterceptor;
import com.tenco.spring_blog_practice._core.intercepter.SessionUserModleInterceptor;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    // 내부 정의한 인터셉터 등록

    private final IpBlockInterceptor ipBlockInterceptor;
    private final LoginInterceptor loginInterceptor;
    private final SessionUserModleInterceptor sessionUserModleInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionUserModleInterceptor).addPathPatterns("/**");
        registry.addInterceptor(loginInterceptor).addPathPatterns("/user/**", "/board/**")
                .excludePathPatterns("/board/list", "/board/{id:\\d+}");
        registry.addInterceptor(ipBlockInterceptor).addPathPatterns("/**");
    }
}
