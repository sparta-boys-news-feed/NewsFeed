package com.spartaboys.newsfeed.domain.auth;

import com.spartaboys.newsfeed.domain.auth.dto.UserPrincipal;
import com.spartaboys.newsfeed.domain.users.entity.User;
import com.spartaboys.newsfeed.domain.users.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private final JwtVerifier verifier;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        log.info("AuthH={}", request.getHeader(HttpHeaders.AUTHORIZATION));

        String token = BearerTokenResolver.resolve(request);

        if (token == null) {
            unauthorized(response, "Authorization 헤더가 없습니다.");
            return;
        }

        try {
            Claims c = verifier.verifyAndGetClaims(token);

            // 1) 토큰에서 닉네임 추출
            String nickname = c.getSubject(); // sub=nickname 기준
            if (nickname == null || nickname.isBlank()) {
                unauthorized(response, "유효한 사용자 식별자가 없습니다."); return;
            }

            // 2) 닉네임으로 사용자 대조 (중복/변경 이슈는 감안)
            Optional<User> user = userRepository.findByNickname(nickname);
            if (user == null) {
                unauthorized(response, "유효한 사용자 식별자가 없습니다."); return;
            }

            // 3) Principal 세팅
            UserPrincipal principal = new UserPrincipal(user.get().getId(), user.get().getNickname());
            request.setAttribute("CURRENT_USER", principal);
            chain.doFilter(request, response);

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.warn("JWT expired at {}", e.getClaims().getExpiration(), e);
            unauthorized(response, "토큰이 만료되었습니다.");
        } catch (io.jsonwebtoken.SignatureException e) {
            log.warn("JWT signature invalid", e);
            unauthorized(response, "서명이 유효하지 않습니다.");
        } catch (io.jsonwebtoken.MalformedJwtException | io.jsonwebtoken.UnsupportedJwtException e) {
            log.warn("JWT malformed/unsupported", e);
            unauthorized(response, "토큰 형식이 잘못되었습니다.");
        } catch (IllegalArgumentException e) {
            log.warn("JWT empty/illegal", e);
            unauthorized(response, "토큰이 비어있습니다.");
        }
    }

    private void unauthorized(HttpServletResponse res, String msg) throws IOException {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write("{\"message\":\"" + msg + "\"}");
    }

    private static Long claimAsLong(Claims c, String name) {
        Object v = c.get(name);
        if (v == null) return null;
        if (v instanceof Number n) return n.longValue();
        try { return Long.parseLong(v.toString()); } catch (Exception ignore) { return null; }
    }

    private static Long parseLongSafe(String s) {
        try { return (s == null) ? null : Long.parseLong(s); }
        catch (Exception e) { return null; }
    }
}
