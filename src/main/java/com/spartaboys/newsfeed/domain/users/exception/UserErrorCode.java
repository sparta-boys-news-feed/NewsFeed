package com.spartaboys.newsfeed.domain.users.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorCode {
    // 0XX  회원가입 관련
    USR_DUPLICATE_EMAIL("USR-001", "중복된 이메일입니다."),
    USR_DUPLICATE_NICKNAME("USR-002", "중복된 닉네임입니다"),

    // 1XX  로그인/인증 관련
    USR_UNAUTHORIZED("USR-100", "인증이 필요합니다");

    private final String code;
    private final String message;
}
