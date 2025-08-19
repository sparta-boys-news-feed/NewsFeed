package com.spartaboys.newsfeed.domain.like.board.exception;

import com.spartaboys.newsfeed.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BoardLikeErrorCode implements ErrorCode {


    /**
     * 참고 : https://geumba.tistory.com/78
     */
    // 409 CONFLICT
    ALREADY_LIKED_BOARD(HttpStatus.CONFLICT, "이미 좋아요를 누른 게시글입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
