package com.rootable.apiserver.config;

import com.rootable.apiserver.formatter.LocalDateFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomServletConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new LocalDateFormatter());
    }

    /*@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //모든 경로 허용
                .maxAge(500) //응답 대기 시간
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS") //모든 메서드 허용
                .allowedOrigins("*"); //모든 소스 허용
    }*/

}
