package com.spartaboys.newsfeed.domain.auth;

public interface JWTConstant {

    String JWT_SECRET = "super-secret-key-change-me-32bytes-minimum-aaaaaaaaaaaa";
    long ACCESS_EXP_MS = 15 * 60 * 1000L;

}
