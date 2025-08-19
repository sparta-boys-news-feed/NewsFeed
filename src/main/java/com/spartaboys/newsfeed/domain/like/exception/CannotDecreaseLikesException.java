package com.spartaboys.newsfeed.domain.like.exception;

import com.spartaboys.newsfeed.common.exception.ErrorCode;
import com.spartaboys.newsfeed.common.exception.GlobalException;

public class CannotDecreaseLikesException extends GlobalException {

    public CannotDecreaseLikesException(ErrorCode errorCode) {
        super(errorCode);
    }
}
