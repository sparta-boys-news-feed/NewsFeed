package com.spartaboys.newsfeed.domain.like.exception;

import com.spartaboys.newsfeed.common.exception.ErrorCode;
import com.spartaboys.newsfeed.common.exception.GlobalException;

public class AlreadyLikedException extends GlobalException {

    public AlreadyLikedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
