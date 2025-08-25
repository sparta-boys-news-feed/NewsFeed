package com.spartaboys.newsfeed.domain.auth;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<TokenFilter> tokenFilterRegistration(TokenFilter filter) {
        var reg = new FilterRegistrationBean<>(filter);
        reg.setOrder(1);                       // 우선순위
        reg.addUrlPatterns("/me", "/auth/logout");
        return reg;
    }
}

