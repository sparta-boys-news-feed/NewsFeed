package com.spartaboys.newsfeed.domain.auth;

import com.fasterxml.jackson.core.filter.TokenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<JwtTokenFilter> tokenFilterRegistration(JwtTokenFilter filter) {
        FilterRegistrationBean<JwtTokenFilter> reg = new FilterRegistrationBean<>(filter);
        reg.setOrder(1);
        reg.addUrlPatterns("/auth/logout");
        return reg;
    }
}

