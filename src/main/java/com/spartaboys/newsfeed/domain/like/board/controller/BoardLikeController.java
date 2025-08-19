package com.spartaboys.newsfeed.domain.like.board.controller;

import com.spartaboys.newsfeed.common.response.ApiResponse;
import com.spartaboys.newsfeed.domain.like.board.service.BoardLikeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards/{boardId}/likes")
@RequiredArgsConstructor
public class BoardLikeController {

    private final BoardLikeQueryService boardLikeQueryService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> registerBoardLike(
            @SessionAttribute(name = "login_id") Long loginId,
            @PathVariable Long boardId
    ) {
        boardLikeQueryService.registerBoardLike(loginId, boardId);
        return ApiResponse.noContent();
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteBoardLike(
            @SessionAttribute(name = "login_id") Long loginId,
            @PathVariable Long boardId
    ) {
        boardLikeQueryService.delete(loginId, boardId);
        return ApiResponse.noContent();
    }

}
