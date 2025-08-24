package com.spartaboys.newsfeed.domain.like.board.controller;

import com.spartaboys.newsfeed.common.response.ApiResponse;
import com.spartaboys.newsfeed.domain.like.board.service.external.BoardLikeCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards/{boardId}/likes")
@RequiredArgsConstructor
public class BoardLikeController {

    private final BoardLikeCommandService boardLikeCommandService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> likeBoard(
            @SessionAttribute(name = "login_id") Long loginId,
            @PathVariable Long boardId
    ) {
        boardLikeCommandService.likeBoard(loginId, boardId);
        return ApiResponse.noContent();
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> unlikeBoard(
            @SessionAttribute(name = "login_id") Long loginId,
            @PathVariable Long boardId
    ) {
        boardLikeCommandService.unlikeBoard(loginId, boardId);
        return ApiResponse.noContent();
    }

}
