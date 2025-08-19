package com.spartaboys.newsfeed.domain.comments.controller;

import com.spartaboys.newsfeed.common.response.ApiPageResponse;
import com.spartaboys.newsfeed.common.response.ApiResponse;
import com.spartaboys.newsfeed.domain.comments.dto.request.CommentCreateRequest;
import com.spartaboys.newsfeed.domain.comments.dto.request.CommentUpdateRequest;
import com.spartaboys.newsfeed.domain.comments.dto.response.CommentResponse;
import com.spartaboys.newsfeed.domain.comments.service.CommentService;
import com.spartaboys.newsfeed.domain.users.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards/{boardId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long boardId,
            @SessionAttribute("user") User loginUser,
            @Valid @RequestBody CommentCreateRequest commentRequest
    ) {

        CommentResponse response = commentService.createComment(boardId, loginUser, commentRequest);

        return ApiResponse.created(response);
    }

    @GetMapping
    public ResponseEntity<ApiPageResponse<CommentResponse>> getAllByBoardId(
            @PathVariable Long boardId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        // 페이지 객체 생성
        Pageable pageable = PageRequest.of(page, size);

        Page<CommentResponse> response = commentService.getAllByBoardId(boardId, pageable);

        return ApiPageResponse.success(response);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> getComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId
    ) {

        CommentResponse response = commentService.getComment(boardId, commentId);

        return ApiResponse.success(response);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @SessionAttribute("user") User loginUser,
            @Valid @RequestBody CommentUpdateRequest commentRequest
    ) {

        CommentResponse response = commentService.updateComment(boardId, commentId, loginUser, commentRequest);

        return ApiResponse.success(response);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @SessionAttribute("user") User loginUser
    ) {
        commentService.deleteComment(boardId, commentId, loginUser);
    }
}
