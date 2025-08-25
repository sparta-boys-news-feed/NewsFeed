package com.spartaboys.newsfeed.domain.auth;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class BearerTokenResolver {

    //Bearer 뺀 토큰 검증
    private BearerTokenResolver() {}
    public static String resolve(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) return null;
        String[] parts = header.split("\\s+", 2); // "Bearer <token>"
        if (parts.length != 2 || !parts[0].equalsIgnoreCase("Bearer")) return null;
        return parts[1].isEmpty() ? null : parts[1];
    }
}
