package com.spartaboys.newsfeed.domain.comments.exception;

import com.spartaboys.newsfeed.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommentErrorCode implements ErrorCode {

    // 0XX 권한 관련
    // 댓글에 대한 권한이 없을 때 UNAUTHORIZED
    FORBIDDEN_COMMENT_ACCESS("CMT-001", HttpStatus.FORBIDDEN, "해당 댓글에 대한 권한이 없습니다."),

    // 1XX 없는 게시물 or 댓글 조회
    // 게시글이 없을 때 (혹은 삭제 됐을 때) NOT_FOUND
    BOARD_NOT_FOUND("CMT-100",HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    // 댓글을 찾을 수 없을 때 NOT_FOUND
    COMMENT_NOT_FOUND("CMT-101",HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),

    // 2XX 잘못된 요청
    // 요청된 게시물 ID 가 해당 댓글의 게시물 ID와 다를 때
    NOT_COMMENT_OF_BOARD("CMT-200",HttpStatus.BAD_REQUEST, "해당 댓글은 지정된 게시글에 속하지 않습니다."),
    // 댓글 내용이 잘못 되었을 때 BAD_REQUEST
    INVALID_COMMENT_CONTENT("CMT-201",HttpStatus.BAD_REQUEST, "댓글 내용이 유효하지 않습니다."),
    // 이미 해당 댓글이 대댓글 일 때 BAD_REQUEST
    REPLY_BAD_REQUEST("CMT-202",HttpStatus.BAD_REQUEST, "대댓글에 댓글을 달 수 없습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
