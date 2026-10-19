package com.tenco.spring_blog_practice._core.config;

import com.tenco.spring_blog_practice._core.intercepter.AdminInterceptor;
import com.tenco.spring_blog_practice._core.intercepter.IpBlockInterceptor;
import com.tenco.spring_blog_practice._core.intercepter.LoginInterceptor;
import com.tenco.spring_blog_practice._core.intercepter.SessionUserModleInterceptor;
import com.tenco.spring_blog_practice._core.util.FileUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    // 내부 정의한 인터셉터 등록

    private final IpBlockInterceptor ipBlockInterceptor;
    private final LoginInterceptor loginInterceptor;
    private final SessionUserModleInterceptor sessionUserModleInterceptor;
    private final AdminInterceptor adminInterceptor;

    // 업로드 폴더의 파일을 /images/** 주소로 제공
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 업로드 폴더 경로를 file:///... 형식의 주소로 변환(운영체제에 맞게 자동 변환됨)
        String location = Paths.get(FileUtil.UPLOAD_DIR).toUri().toString();
        // 윈도우 기준 경로 : C:\Users\사용자명\blog_uploads\

        // 폴더 위치는 반드시 / 로 끝나야 그 안의 파일을 찾을 수 있다.
        if (!location.endsWith("/")) {
            location = location + "/";
        }
        registry.addResourceHandler("/images/**").addResourceLocations(location);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionUserModleInterceptor).addPathPatterns("/**");
        registry.addInterceptor(loginInterceptor).addPathPatterns("/user/**", "/board/**", "/reply/**", "/admin/**")
                .excludePathPatterns("/board/list", "/board/{id:\\d+}");
        registry.addInterceptor(ipBlockInterceptor).addPathPatterns("/**");
        registry.addInterceptor(adminInterceptor).addPathPatterns("/admin/**");
    }
}
