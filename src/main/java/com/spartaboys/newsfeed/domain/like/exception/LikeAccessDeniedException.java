package com.spartaboys.newsfeed.domain.like.exception;

import com.spartaboys.newsfeed.common.exception.ErrorCode;
import com.spartaboys.newsfeed.common.exception.GlobalException;

public class LikeAccessDeniedException extends GlobalException {

    public LikeAccessDeniedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
