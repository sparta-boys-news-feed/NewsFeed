package com.spartaboys.newsfeed.domain.follow.exception;

import com.spartaboys.newsfeed.common.exception.ErrorCode;
import com.spartaboys.newsfeed.common.exception.GlobalException;

public class AlreadyFollowingException extends GlobalException {

    public AlreadyFollowingException(ErrorCode errorCode) {
        super(errorCode);
    }
}
