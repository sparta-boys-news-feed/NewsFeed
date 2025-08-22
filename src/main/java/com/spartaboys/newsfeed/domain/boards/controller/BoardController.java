package com.spartaboys.newsfeed.domain.boards.controller;

import com.spartaboys.newsfeed.common.response.ApiPageResponse;
import com.spartaboys.newsfeed.common.response.ApiResponse;
import com.spartaboys.newsfeed.domain.boards.dto.request.BoardRequest;
import com.spartaboys.newsfeed.domain.boards.dto.response.BoardResponse;
import com.spartaboys.newsfeed.domain.boards.entity.Board;
import com.spartaboys.newsfeed.domain.boards.service.BoardExternalService;
import com.spartaboys.newsfeed.domain.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
    private final BoardExternalService boardExternalService;

    @PostMapping
    public ResponseEntity<ApiResponse<BoardResponse>> getBoardByUserId(@Validated @RequestBody BoardRequest request,
                                                                       @SessionAttribute(value = "LOGIN_USER_ID") Long loginUserId) {
        return ApiResponse.created(boardExternalService.getBoardByUserId(request, loginUserId));
    }

    @GetMapping
    public ResponseEntity<ApiPageResponse<BoardResponse>> getAllBoards(@RequestParam(required = false, defaultValue = "0") int page,
                                                                 @RequestParam(required = false, defaultValue = "10") int size) {
        // Pageable 객체 생성(현재 정책상 Client는 page, size, Service는 sort를 담당하고 있으나 추후 확장성을 고려하여 컨트롤에서 생성)
        // 생성일 기준 내림차순으로 정렬
        Pageable pageable = PageRequest.of(page, size, Sort.sort(Board.class).by(Board::getCreatedAt).descending());

        return ApiPageResponse.success(boardExternalService.getAllBoards(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiPageResponse<BoardResponse>> getBoardsByTitleOrContent(@RequestParam(required = false, defaultValue = "0") int page,
                                                                                    @RequestParam(required = false, defaultValue = "10") int size,
                                                                                    @RequestParam(required = false) String title,
                                                                                    @RequestParam(required = false) String content) {
        // Pageable 객체 생성(현재 정책상 Client는 page, size, Service는 sort를 담당하고 있으나 추후 확장성을 고려하여 컨트롤에서 생성)
        // 생성일 기준 내림차순으로 정렬
        Pageable pageable = PageRequest.of(page, size, Sort.sort(Board.class).by(Board::getCreatedAt).descending());

        return ApiPageResponse.success(boardExternalService.getBoardsByTitleOrContent(pageable, title, content));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<BoardResponse>> getBoardByBoardId(@PathVariable Long boardId) {
        return ApiResponse.success(boardExternalService.getBoardByBoardId(boardId));
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<ApiResponse<BoardResponse>> updateBoardDetailsByBoardId(@PathVariable Long boardId,
                                                                                  @SessionAttribute(value = "LOGIN_USER_ID") Long loginUserId,
                                                                                  @Validated @RequestBody BoardRequest request) {
        return ApiResponse.success(boardExternalService.updateBoardDetailsByBoardId(boardId, loginUser, request));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponse<Object>> deleteBoardByBoardId(@PathVariable Long boardId,
                                                                    @SessionAttribute(value = "LOGIN_USER_ID") Long loginUserId) {
        boardExternalService.deleteBoardByBoardId(boardId, loginUserId);

        return ApiResponse.noContent();
    }
}
