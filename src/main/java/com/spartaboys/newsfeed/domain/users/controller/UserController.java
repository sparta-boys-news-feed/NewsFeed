package com.spartaboys.newsfeed.domain.users.controller;

import com.spartaboys.newsfeed.common.exception.ErrorCode;
import com.spartaboys.newsfeed.domain.users.dto.request.ChangePasswordRequest;
import com.spartaboys.newsfeed.domain.users.dto.request.UserUpdateRequest;
import com.spartaboys.newsfeed.domain.users.dto.response.UserPrivateResponse;
import com.spartaboys.newsfeed.domain.users.dto.response.UserPublicResponse;
import com.spartaboys.newsfeed.domain.users.dto.response.UserUpdateResponse;
import com.spartaboys.newsfeed.domain.users.exception.InvalidUserException;
import com.spartaboys.newsfeed.domain.users.exception.UserErrorCode;
import com.spartaboys.newsfeed.domain.users.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private Long getUserIdFromSession(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new InvalidUserException(UserErrorCode.USR_UNAUTHORIZED);
        }

        return userId;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserPublicResponse> getUserById(@PathVariable Long userId) {
        UserPublicResponse publicUserInfo = userService.getPublicUserById(userId);

        return ResponseEntity.ok(publicUserInfo);
    }

    // 임의로 세션을 통해 구현
    @GetMapping("/me")
    public ResponseEntity<UserPrivateResponse> getCurrentUser(
            HttpSession session
    ) {
        UserPrivateResponse privateUserInfo = userService.getPrivateUserById(
                getUserIdFromSession(session)
        );

        return ResponseEntity.ok(privateUserInfo);
    }

    @PatchMapping("/me")
    public ResponseEntity<UserUpdateResponse> updateCurrentUser(
            HttpSession session,
            @Valid @RequestBody UserUpdateRequest dto
    ) {
        UserUpdateResponse updateUserInfo = userService.updateUserProfile(
                getUserIdFromSession(session), dto
        );

        return ResponseEntity.ok(updateUserInfo);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> updatePassword(
            HttpSession session,
            @Valid @RequestBody ChangePasswordRequest dto
    ) {
        userService.updateUserPassword(getUserIdFromSession(session), dto);

        return ResponseEntity.noContent().build();
    }


    //@GetMapping("/me")

}
