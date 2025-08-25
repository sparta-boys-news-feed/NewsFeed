package com.spartaboys.newsfeed.domain.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Bean
    public JwtVerifier jwtVerifier(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.clock-skew-seconds:60}") long skewSec
    ) {
        return new JwtVerifier(secret, null, null, skewSec);
    }
}
