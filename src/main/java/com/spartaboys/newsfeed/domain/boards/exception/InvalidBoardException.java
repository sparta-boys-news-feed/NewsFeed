package com.spartaboys.newsfeed.domain.boards.exception;

import com.spartaboys.newsfeed.common.exception.ErrorCode;
import com.spartaboys.newsfeed.common.exception.GlobalException;

public class InvalidBoardException extends GlobalException {
    public InvalidBoardException(ErrorCode errorCode) {
        super(errorCode);
    }
}
