package com.spartaboys.newsfeed.domain.follow.exception;

import com.spartaboys.newsfeed.common.exception.ErrorCode;
import com.spartaboys.newsfeed.common.exception.GlobalException;

public class FollowNotFoundException extends GlobalException {

    public FollowNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
