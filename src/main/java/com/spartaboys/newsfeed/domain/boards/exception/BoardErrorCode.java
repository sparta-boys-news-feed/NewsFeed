package com.spartaboys.newsfeed.domain.boards.exception;

import com.spartaboys.newsfeed.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BoardErrorCode implements ErrorCode {
    // 0XX  권한 관련
    // 403 Forbidden, 게시글 작성자 Id와 로그인 유저 Id가 다를 경우에 대한 예외
    BOARD_FORBIDDEN("BRD-001", HttpStatus.FORBIDDEN, "해당 게시글에 대한 권한이 없습니다."),

    // 1XX  조회 관련
    // 400 Bad Request, Db에 게시글Id가 존재하지 않는 경우에 대한 예외
    BOARD_NOT_FOUND("BRD-101", HttpStatus.BAD_REQUEST, "존재하지 않는 게시글입니다."),
    // 400 Bad Request, 제목 또는 내용으로 게시글을 조회하는 경우 파라미터가 모두 존재하거나 모두 null인 경우에 대한 예외
    BOARD_INVALID_SEARCH_NOT_ALLOWED("BRD-102", HttpStatus.BAD_REQUEST, "제목과 내용 중 하나만 입력해주세요."),

    // 2XX  삭제 또는 수정 관련
    // 410 Gone, Db에 수정 또는 삭제하고자 하는 게시글이 존재하지만 삭제된 게시글(isDeleted = true)인 경우에 대한 예외
    BOARD_ALREADY_DELETED("BRD-201", HttpStatus.GONE, "이미 삭제된 게시글입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
