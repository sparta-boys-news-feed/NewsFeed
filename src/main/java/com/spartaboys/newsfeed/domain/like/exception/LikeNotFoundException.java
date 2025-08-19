package com.spartaboys.newsfeed.domain.like.exception;

import com.spartaboys.newsfeed.common.exception.ErrorCode;
import com.spartaboys.newsfeed.common.exception.GlobalException;

public class LikeNotFoundException extends GlobalException {

    public LikeNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
