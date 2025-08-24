package com.spartaboys.newsfeed.domain.comments.exception;

import com.spartaboys.newsfeed.common.exception.ErrorCode;
import com.spartaboys.newsfeed.common.exception.GlobalException;

public class InvalidCommentException extends GlobalException {
    public InvalidCommentException(ErrorCode errorCode) {
      super(errorCode);
    }
}
