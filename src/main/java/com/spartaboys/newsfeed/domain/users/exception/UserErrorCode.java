package com.spartaboys.newsfeed.domain.users.exception;

import com.spartaboys.newsfeed.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {
    // 0XX  회원가입/멤버 정보 관련
    USR_DUPLICATE_EMAIL("USR-001", HttpStatus.CONFLICT, "중복된 이메일입니다."),
    USR_DUPLICATE_NICKNAME("USR-002", HttpStatus.CONFLICT, "중복된 닉네임입니다"),
    USR_PW_CHECK_MISMATCH("USR-004", HttpStatus.BAD_REQUEST, "새 비밀번호와 비밀번호 확인값이 일치하지 않습니다"),


    // 1XX  로그인/인증 관련
    USR_UNAUTHORIZED("USR-100", HttpStatus.UNAUTHORIZED, "인증이 필요합니다"),
    USR_DELETED("USR-101", HttpStatus.BAD_REQUEST, "삭제된 유저입니다"),
    USR_PW_CURRENT_MISMATCH("USR-102", HttpStatus.UNAUTHORIZED, "현재 비밀번호가 일치하지 않습니다"),

    // 2XX 검색 관련
    USR_INVALID_ID("USR-200", HttpStatus.BAD_REQUEST, "유효하지 않은 id값 입니다"),
    USR_INVALID_EMAIL("USR-201", HttpStatus.BAD_REQUEST, "유효하지 않은 email 입니다"),
    USR_INVALID_NICKNAME("USR-202", HttpStatus.BAD_REQUEST, "유효하지 않은 nickname 입니다");



    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
