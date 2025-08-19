package com.spartaboys.newsfeed.domain.like.exception;

import com.spartaboys.newsfeed.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LikeErrorCode implements ErrorCode {

    // 400 BAD_REQUEST
    CANNOT_DECREASE_LIKES(HttpStatus.BAD_REQUEST, "좋아요 수는 0 미만이 될 수 없습니다."),

    // 403 FORBIDDEN
    NOT_LIKE_OWNER(HttpStatus.FORBIDDEN, "해당 좋아요의 소유자가 아닙니다."),

    // 404 NOT_FOUND
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요 정보를 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
