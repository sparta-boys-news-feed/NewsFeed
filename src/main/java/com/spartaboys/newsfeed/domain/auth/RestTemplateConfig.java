package com.spartaboys.newsfeed.domain.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;
import java.util.Objects;

@Configuration
public class RestTemplateConfig {

    private static final String ACCESS_TOKEN_COOKIE = "ACCESS_TOKEN"; // 쿠키 이름을 네가 사용 중인 이름으로 맞춰줘

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate rt = new RestTemplate();

        rt.getInterceptors().add((request, body, execution) -> {
            String bearer = currentBearer(); // "Bearer xxx" 또는 null
            if (bearer != null) {
                request.getHeaders().set(HttpHeaders.AUTHORIZATION, bearer);
            }
            return execution.execute(request, body);
        });

        return rt;
    }

    /**
     * 현재 쓰레드의 HTTP 요청 컨텍스트에서 Bearer 토큰을 찾아 "Bearer xxx" 형태로 반환.
     * 우선순위: Authorization 헤더 → ACCESS_TOKEN 쿠키 → (선택) Request Attribute
     */
    private String currentBearer() {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) return null;

        HttpServletRequest req = attrs.getRequest();

        // 1) Authorization 헤더 그대로 사용 (이미 "Bearer xxx" 형태일 것)
        String h = headerIgnoreCase(req, HttpHeaders.AUTHORIZATION);
        if (h != null && !h.isBlank()) {
            return h.trim();
        }

        // 2) 쿠키에서 액세스 토큰 사용 (쿠키명은 프로젝트에 맞춰 수정)
        String token = tokenFromCookie(req, ACCESS_TOKEN_COOKIE);
        if (token != null) {
            return "Bearer " + token;
        }

        // 3) (옵션) 필터에서 저장해둔 요청 어트리뷰트가 있다면 사용
        Object raw = req.getAttribute("CURRENT_USER_TOKEN");
        if (raw instanceof String s && !s.isBlank()) {
            return s.startsWith("Bearer ") ? s : ("Bearer " + s);
        }

        return null;
    }

    private static String headerIgnoreCase(HttpServletRequest req, String name) {
        Enumeration<String> names = req.getHeaderNames();
        if (names == null) return null;
        while (names.hasMoreElements()) {
            String n = names.nextElement();
            if (name.equalsIgnoreCase(n)) {
                return req.getHeader(n);
            }
        }
        return null;
    }

    private static String tokenFromCookie(HttpServletRequest req, String cookieName) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) return null;
        for (Cookie c : cookies) {
            if (Objects.equals(cookieName, c.getName()) && c.getValue() != null && !c.getValue().isBlank()) {
                return c.getValue().trim();
            }
        }
        return null;
    }
}


