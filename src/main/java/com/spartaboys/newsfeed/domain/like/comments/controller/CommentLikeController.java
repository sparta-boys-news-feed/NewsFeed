package com.spartaboys.newsfeed.domain.like.comments.controller;

import com.spartaboys.newsfeed.common.response.ApiResponse;
import com.spartaboys.newsfeed.domain.like.comments.service.external.CommentLikeCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards/{boardId}/comments/{commentId}/likes")
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeCommandService commentLikeCommandService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> likeComment(
            @SessionAttribute(name = "LOGIN_USER_ID") Long loginId,
            @PathVariable Long boardId,
            @PathVariable Long commentId
    ) {
        commentLikeCommandService.likeComment(loginId, boardId, commentId);
        return ApiResponse.noContent();
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> unlikeComment(
            @SessionAttribute(name = "LOGIN_USER_ID") Long loginId,
            @PathVariable Long boardId,
            @PathVariable Long commentId
    ) {
        commentLikeCommandService.unlikeComment(loginId, boardId, commentId);
        return ApiResponse.noContent();
    }

}
