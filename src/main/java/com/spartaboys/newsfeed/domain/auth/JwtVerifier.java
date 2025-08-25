package com.spartaboys.newsfeed.domain.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Slf4j
public class JwtVerifier {
    private final Key key;
    private final long clockSkewSeconds;

    private static String hash12(String s){
        try {
            var md = java.security.MessageDigest.getInstance("SHA-256");
            var d  = md.digest(s.getBytes(StandardCharsets.UTF_8));
            return java.util.Base64.getEncoder().encodeToString(d).substring(0, 12);
        } catch (Exception e) { return "NA"; }
    }

    public JwtVerifier(String secret, String issuerIgnored, String audIgnored, long skewSec) {
        // 공백/개행 제거
        secret = secret == null ? "" : secret.trim();

        // 생성/검증 동일성 확인용 로그 (길이 + 해시)
        log.info("[JWT-VER] secretLen={}, secretHash={}", secret.length(), hash12(secret));

        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.clockSkewSeconds = skewSec;
    }

    public Claims verifyAndGetClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .setAllowedClockSkewSeconds(clockSkewSeconds)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
