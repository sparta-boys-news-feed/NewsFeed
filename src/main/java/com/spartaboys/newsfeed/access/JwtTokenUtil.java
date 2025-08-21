package com.spartaboys.newsfeed.access;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;

// Jwt 활용할 기능들 정리  토큰 생성.
public class JwtTokenUtil {

    public static String createToken(String loginId, String key, long expireTimeMs){

        // Claim = Jwt Token에 들어갈 정보
        Date now = new Date();
        Claims claims = Jwts.claims();
        claims.put("loginId", loginId);
        claims.setSubject(loginId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, key.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    // Claims에서 loginId 꺼내기
    public static String getLoginId(String token, String secretKey){
        return extractClaims(token, secretKey).get("loginId", String.class);
    }

    // 발급된 Token이 만료 시간이 지났는지 체크
    public static boolean isExpired(String token, String secretKey){
        Date expiredDate = extractClaims(token, secretKey).getExpiration();
        //Token의 만료 날짜가 지금보다 이전인지
        return expiredDate.before(new Date());
    }

    //SecretKey를 사용해 Token Parsing
    private static Claims extractClaims(String token, String secretKey){
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
    }
}
