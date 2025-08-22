package com.spartaboys.newsfeed.domain.users.controller;

import com.spartaboys.newsfeed.common.response.ApiPageResponse;
import com.spartaboys.newsfeed.common.response.ApiResponse;
import com.spartaboys.newsfeed.domain.auth.JwtTokenUtil;
import com.spartaboys.newsfeed.domain.boards.dto.response.BoardResponse;
import com.spartaboys.newsfeed.domain.comments.dto.response.CommentResponse;
import com.spartaboys.newsfeed.domain.follow.dto.FollowerResponse;
import com.spartaboys.newsfeed.domain.follow.dto.FollowingResponse;
import com.spartaboys.newsfeed.domain.like.dto.LikeResponse;
import com.spartaboys.newsfeed.domain.users.dto.request.ChangePasswordRequest;
import com.spartaboys.newsfeed.domain.users.dto.request.UserUpdateRequest;
import com.spartaboys.newsfeed.domain.users.dto.response.UserPrivateResponse;
import com.spartaboys.newsfeed.domain.users.dto.response.UserPublicResponse;
import com.spartaboys.newsfeed.domain.users.dto.response.UserUpdateResponse;
import com.spartaboys.newsfeed.domain.users.exception.InvalidUserException;
import com.spartaboys.newsfeed.domain.users.exception.UserErrorCode;
import com.spartaboys.newsfeed.domain.users.service.UserExternalService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserExternalService userService;

    private Long getUserIdFromRequest(HttpServletRequest request) {
        // TODO : Temp 사용이므로 추후 꼭 수정할것!
        String secretKey = "super-secret-key-change-me-32bytes-minimum-aaaaaaaaaaaa";

        return Long.valueOf(JwtTokenUtil.getLoginId(
                request.getHeader(HttpHeaders.AUTHORIZATION).substring(7),
                secretKey));

    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserPublicResponse>>> getPublicUsersByNicknameContaining(
            @RequestParam(defaultValue = "") String nickname
    ) {
        List<UserPublicResponse> searchResults =
                userService.getPublicUsersByNicknameContaining(nickname);

        return ApiResponse.success(searchResults);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserPublicResponse>> getUserById(
            @PathVariable Long userId
    ) {

        UserPublicResponse publicUserInfo = userService.getPublicUserById(userId);

        return ApiResponse.success(publicUserInfo);
    }

    @GetMapping("/{userId}/boards")
    public ResponseEntity<ApiPageResponse<BoardResponse>> getBoardsByUserId(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable Long userId
    ) {
        Page<BoardResponse> page = userService.getBoardsByUserId(pageable, userId);

        return ApiPageResponse.success(page);
    }

    @GetMapping("/{userId}/comments")
    public ResponseEntity<ApiPageResponse<CommentResponse>> getCommentsByUserId(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable Long userId
    ) {
        Page<CommentResponse> page = userService.getCommentsByUserId(pageable, userId);

        return ApiPageResponse.success(page);
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<ApiPageResponse<FollowerResponse>> getFollowersByUserId(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable Long userId
    ) {
        Page<FollowerResponse> page = userService.getFollowersFromUserId(pageable, userId);

        return ApiPageResponse.success(page);
    }

    @GetMapping("/{userId}/followees")
    public ResponseEntity<ApiPageResponse<FollowingResponse>> getFolloweesByUserId(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @PathVariable Long userId
    ) {
        Page<FollowingResponse> page = userService.getFolloweesFromUserId(pageable, userId);

        return ApiPageResponse.success(page);
    }

    // TODO: 임의로 세션을 통해 구현 (추후 로직 변경 시 반영)
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserPrivateResponse>> getCurrentUser(
            HttpServletRequest request
    ) {
        UserPrivateResponse privateUserInfo = userService.getPrivateUserById(
                getUserIdFromRequest(request)
        );

        return ApiResponse.success(privateUserInfo);
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UserUpdateResponse>> updateCurrentUser(
            HttpServletRequest request,
            @Valid @RequestBody UserUpdateRequest dto
    ) {
        UserUpdateResponse updateUserInfo = userService.updateUserProfile(
                getUserIdFromRequest(request), dto
        );

        return ApiResponse.success(updateUserInfo);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(
            HttpServletRequest request,
            @Valid @RequestBody ChangePasswordRequest dto
    ) {
        userService.updateUserPassword(getUserIdFromRequest(request), dto);

        return ApiResponse.noContent();
    }


    @GetMapping("/me/boards")
    public ResponseEntity<ApiPageResponse<BoardResponse>> getBoardsByMe(
            HttpServletRequest request,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<BoardResponse> page = userService.getBoardsByUserId(pageable, getUserIdFromRequest(request));

        return ApiPageResponse.success(page);
    }

    @GetMapping("/me/comments")
    public ResponseEntity<ApiPageResponse<CommentResponse>> getCommentsByMe(
            HttpServletRequest request,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<CommentResponse> page = userService.getCommentsByUserId(pageable, getUserIdFromRequest(request));

        return ApiPageResponse.success(page);
    }

    @GetMapping("/me/followers")
    public ResponseEntity<ApiPageResponse<FollowerResponse>> getFollowersByMe(
            HttpServletRequest request,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<FollowerResponse> page = userService.getFollowersFromUserId(pageable, getUserIdFromRequest(request));

        return ApiPageResponse.success(page);
    }

    @GetMapping("/me/followees")
    public ResponseEntity<ApiPageResponse<FollowingResponse>> getFolloweesByUserId(
            HttpServletRequest request,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<FollowingResponse> page = userService.getFolloweesFromUserId(pageable, getUserIdFromRequest(request));

        return ApiPageResponse.success(page);
    }

    // board: 게시글 | comment: 댓글 | all(or 다른값): 전체
    @GetMapping("/me/likes")
    public ResponseEntity<ApiResponse<List<LikeResponse>>> getLikesByUserId(
            HttpServletRequest request,
            @RequestParam(defaultValue = "all") String target
    ) {
        List<LikeResponse> likes = userService.getLikesFromUserId(getUserIdFromRequest(request), target);

        return ApiResponse.success(likes);
    }
}
