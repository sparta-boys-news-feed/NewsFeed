package com.spartaboys.newsfeed.domain.users.exception;

import com.spartaboys.newsfeed.common.exception.ErrorCode;
import com.spartaboys.newsfeed.common.exception.GlobalException;

public class InvalidUserException extends GlobalException {

    public InvalidUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
