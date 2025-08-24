package com.spartaboys.newsfeed.domain.like.comments.exception;

import com.spartaboys.newsfeed.common.exception.ErrorCode;
import com.spartaboys.newsfeed.common.exception.GlobalException;

public class NotCommentOfBoardException extends GlobalException {

    public NotCommentOfBoardException(ErrorCode errorCode) {
        super(errorCode);
    }
}
