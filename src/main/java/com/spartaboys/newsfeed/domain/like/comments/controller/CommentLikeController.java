package com.spartaboys.newsfeed.domain.like.comments.controller;

import com.spartaboys.newsfeed.common.response.ApiResponse;
import com.spartaboys.newsfeed.domain.like.comments.service.CommentLikeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards/{boardId}/comments/{commentId}/likes")
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeQueryService commentLikeQueryService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> likeComment(
            @SessionAttribute(name = "login_id") Long loginId,
            @PathVariable Long boardId,
            @PathVariable Long commentId
    ) {
        commentLikeQueryService.likeComment(loginId, boardId, commentId);
        return ApiResponse.noContent();
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> unlikeComment(
            @SessionAttribute(name = "login_id") Long loginId,
            @PathVariable Long boardId,
            @PathVariable Long commentId
    ) {
        commentLikeQueryService.unlikeComment(loginId, boardId, commentId);
        return ApiResponse.noContent();
    }

}
