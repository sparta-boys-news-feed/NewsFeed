package com.spartaboys.newsfeed.domain.comments.exception;

import com.spartaboys.newsfeed.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode implements ErrorCode {

    // 댓글을 찾을 수 없을 때 NOT_FOUND
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),

    // 댓글 내용이 잘못 되었을 때 BAD_REQUSET
    INVALID_COMMENT_CONTENT(HttpStatus.BAD_REQUEST, "댓글 내용이 유효하지 않습니다."),

    // 댓글에 대한 권한이 없을 때 UNAUTHORIZED
    UNAUTHORIZED_COMMENT_ACCESS(HttpStatus.UNAUTHORIZED, "해당 댓글에 대한 권한이 없습니다."),

    // 게시글 ID와 댓글의 게시글 ID가 일치하지 않을 때
    BOARD_BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 접근입니다."),

    // 게시글이 없을 때 (혹은 삭제 됐을 때) NOT_FOUND
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return this.status;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
