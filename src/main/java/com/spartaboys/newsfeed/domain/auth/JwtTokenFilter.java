package com.spartaboys.newsfeed.domain.auth;

import com.spartaboys.newsfeed.domain.auth.service.AccessService;
import com.spartaboys.newsfeed.domain.users.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//OncePerRequestFilter
@FilterRegistration
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final AccessService accessService;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null) {
            filterChain.doFilter(request, response); // dofilter를 사용해서 예외처리
            return;
        }
        if (!authorizationHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response); // dofilter를 사용해서 예외처리
            return;
        }
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);

        try {
            if (JwtTokenUtil.isExpired(token, secretKey)) {
                filterChain.doFilter(request, response);
                return;
            }
            String loginId = JwtTokenUtil.getLoginId(token, secretKey);
            User loginUser = accessService.getLoginUserByLoginId(loginId);
            if (loginUser == null) {
                filterChain.doFilter(request, response);
                return;
            }
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
            filterChain.doFilter(request, response);
            return;
        }

    }

}
