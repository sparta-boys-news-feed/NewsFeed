package com.spartaboys.newsfeed.domain.like.comments.exception;

import com.spartaboys.newsfeed.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommentLikeErrorCode implements ErrorCode {


    ALREADY_LIKED_COMMENT(HttpStatus.CONFLICT, "이미 좋아요를 누른 댓글입니다. " );

    private final HttpStatus httpStatus;
    private final String message;
}
