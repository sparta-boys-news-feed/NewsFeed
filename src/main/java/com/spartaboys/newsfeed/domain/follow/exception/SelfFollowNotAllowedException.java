package com.spartaboys.newsfeed.domain.follow.exception;

import com.spartaboys.newsfeed.common.exception.ErrorCode;
import com.spartaboys.newsfeed.common.exception.GlobalException;

public class SelfFollowNotAllowedException extends GlobalException {

    public SelfFollowNotAllowedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
