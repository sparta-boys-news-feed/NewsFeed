package com.spartaboys.newsfeed.domain.follow.exception;

import com.spartaboys.newsfeed.common.exception.ErrorCode;
import com.spartaboys.newsfeed.common.exception.GlobalException;

public class SelfUnFollowNotAllowedException extends GlobalException {

    public SelfUnFollowNotAllowedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
