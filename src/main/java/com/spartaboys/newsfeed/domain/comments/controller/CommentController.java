package com.spartaboys.newsfeed.domain.comments.controller;

import com.spartaboys.newsfeed.common.response.ApiPageResponse;
import com.spartaboys.newsfeed.common.response.ApiResponse;
import com.spartaboys.newsfeed.domain.comments.dto.request.CommentCreateRequest;
import com.spartaboys.newsfeed.domain.comments.dto.request.CommentUpdateRequest;
import com.spartaboys.newsfeed.domain.comments.dto.response.CommentGetAllResponse;
import com.spartaboys.newsfeed.domain.comments.dto.response.CommentResponse;
import com.spartaboys.newsfeed.domain.comments.service.CommentExternalService;
import com.spartaboys.newsfeed.domain.users.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards/{boardId}/comments")
public class CommentController {

    private final CommentExternalService commentExternalService;

    @PostMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @SessionAttribute("user") User loginUser,
            @Valid @RequestBody CommentCreateRequest commentRequest
    ) {

        CommentResponse response = commentExternalService.createComment(boardId, commentId, loginUser, commentRequest);

        return ApiResponse.created(response);
    }

    @GetMapping
    public ResponseEntity<ApiPageResponse<CommentGetAllResponse>> getAllByBoardId(
            @PathVariable Long boardId,
            @PageableDefault (page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable
    ) {

        Page<CommentGetAllResponse> response = commentExternalService.getAllByBoardId(boardId, pageable);

        return ApiPageResponse.success(response);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> getComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId
    ) {

        CommentResponse response = commentExternalService.getComment(boardId, commentId);

        return ApiResponse.success(response);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @SessionAttribute("user") User loginUser,
            @Valid @RequestBody CommentUpdateRequest commentRequest
    ) {

        CommentResponse response = commentExternalService.updateComment(boardId, commentId, loginUser, commentRequest);

        return ApiResponse.success(response);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @SessionAttribute("user") User loginUser
    ) {
        commentExternalService.deleteComment(boardId, commentId, loginUser);
    }
}
