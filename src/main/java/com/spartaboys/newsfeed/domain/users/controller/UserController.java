package com.spartaboys.newsfeed.domain.users.controller;

import com.spartaboys.newsfeed.common.response.ApiPageResponse;
import com.spartaboys.newsfeed.common.response.ApiResponse;
import com.spartaboys.newsfeed.domain.boards.dto.response.BoardResponse;
import com.spartaboys.newsfeed.domain.comments.dto.response.CommentResponse;
import com.spartaboys.newsfeed.domain.follow.dto.FollowerResponse;
import com.spartaboys.newsfeed.domain.follow.dto.FollowingResponse;
import com.spartaboys.newsfeed.domain.users.dto.request.ChangePasswordRequest;
import com.spartaboys.newsfeed.domain.users.dto.request.UserUpdateRequest;
import com.spartaboys.newsfeed.domain.users.dto.response.UserPrivateResponse;
import com.spartaboys.newsfeed.domain.users.dto.response.UserPublicResponse;
import com.spartaboys.newsfeed.domain.users.dto.response.UserUpdateResponse;
import com.spartaboys.newsfeed.domain.users.exception.InvalidUserException;
import com.spartaboys.newsfeed.domain.users.exception.UserErrorCode;
import com.spartaboys.newsfeed.domain.users.service.UserExternalService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserExternalService userService;

    private Long getUserIdFromSession(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new InvalidUserException(UserErrorCode.USR_UNAUTHORIZED);
        }

        return userId;
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
            HttpSession session
    ) {
        UserPrivateResponse privateUserInfo = userService.getPrivateUserById(
                getUserIdFromSession(session)
        );

        return ApiResponse.success(privateUserInfo);
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UserUpdateResponse>> updateCurrentUser(
            HttpSession session,
            @Valid @RequestBody UserUpdateRequest dto
    ) {
        UserUpdateResponse updateUserInfo = userService.updateUserProfile(
                getUserIdFromSession(session), dto
        );

        return ApiResponse.success(updateUserInfo);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(
            HttpSession session,
            @Valid @RequestBody ChangePasswordRequest dto
    ) {
        userService.updateUserPassword(getUserIdFromSession(session), dto);

        return ApiResponse.noContent();
    }


    @GetMapping("/me/boards")
    public ResponseEntity<ApiPageResponse<BoardResponse>> getBoardsByMe(
            HttpSession session,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<BoardResponse> page = userService.getBoardsByUserId(pageable, getUserIdFromSession(session));

        return ApiPageResponse.success(page);
    }

    @GetMapping("/me/comments")
    public ResponseEntity<ApiPageResponse<CommentResponse>> getCommentsByMe(
            HttpSession session,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<CommentResponse> page = userService.getCommentsByUserId(pageable, getUserIdFromSession(session));

        return ApiPageResponse.success(page);
    }

    @GetMapping("/me/followers")
    public ResponseEntity<ApiPageResponse<FollowerResponse>> getFollowersByMe(
            HttpSession session,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<FollowerResponse> page = userService.getFollowersFromUserId(pageable, getUserIdFromSession(session));

        return ApiPageResponse.success(page);
    }

    @GetMapping("/me/followees")
    public ResponseEntity<ApiPageResponse<FollowingResponse>> getFolloweesByUserId(
            HttpSession session,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<FollowingResponse> page = userService.getFolloweesFromUserId(pageable, getUserIdFromSession(session));

        return ApiPageResponse.success(page);
    }
}
