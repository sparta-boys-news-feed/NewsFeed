package com.spartaboys.newsfeed.domain.follow.controller;

import com.spartaboys.newsfeed.common.response.ApiResponse;
import com.spartaboys.newsfeed.domain.follow.service.FollowCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/me")
@RequiredArgsConstructor
public class FollowController {

    private final FollowCommandService followCommandService;

    @PostMapping("/following/{followeeId}")
    public ResponseEntity<ApiResponse<Void>> followUser(
            @SessionAttribute(name = "login_id") Long followerId,
            @PathVariable Long followeeId
    ) {
        followCommandService.followUser(followerId, followeeId);
        return ApiResponse.noContent();
    }

    @DeleteMapping("/following/{followeeId}")
    public ResponseEntity<ApiResponse<Void>> unFollowUser(
            @SessionAttribute(name = "login_id") Long followerId,
            @PathVariable Long followeeId
    ) {
        followCommandService.unFollowUser(followerId, followeeId);
        return ApiResponse.noContent();
    }

//    @GetMapping("/following")
//    public ResponseEntity<ApiResponse<?>> getFollowingList(@SessionAttribute(name = "login_id") Long loginId) {
//    }
}
