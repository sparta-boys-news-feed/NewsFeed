package com.spartaboys.newsfeed.domain.follow.controller;

import com.spartaboys.newsfeed.common.response.ApiPageResponse;
import com.spartaboys.newsfeed.common.response.ApiResponse;
import com.spartaboys.newsfeed.domain.follow.dto.FollowerResponse;
import com.spartaboys.newsfeed.domain.follow.dto.FollowingResponse;
import com.spartaboys.newsfeed.domain.follow.service.external.FollowCommandService;
import com.spartaboys.newsfeed.domain.follow.service.external.FollowQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/me")
@RequiredArgsConstructor
public class FollowController {

    private final FollowCommandService followCommandService;
    private final FollowQueryService followQueryService;

    @PostMapping("/following/{followeeId}")
    public ResponseEntity<ApiResponse<Void>> followUser(
            @SessionAttribute(name = "LOGIN_USER_ID") Long followerId,
            @PathVariable Long followeeId
    ) {
        followCommandService.followUser(followerId, followeeId);
        return ApiResponse.noContent();
    }

    @DeleteMapping("/following/{followeeId}")
    public ResponseEntity<ApiResponse<Void>> unFollowUser(
            @SessionAttribute(name = "LOGIN_USER_ID") Long followerId,
            @PathVariable Long followeeId
    ) {
        followCommandService.unFollowUser(followerId, followeeId);
        return ApiResponse.noContent();
    }


    @GetMapping("/following")
    public ResponseEntity<ApiPageResponse<FollowingResponse>> getFollowings(
            @SessionAttribute(name = "LOGIN_USER_ID") Long loginId,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Page<FollowingResponse> followingResponses = followQueryService.getFollowings(loginId, pageable);
        return ApiPageResponse.success(followingResponses);
    }


    @GetMapping("/followers")
    public ResponseEntity<ApiPageResponse<FollowerResponse>> getFollowers(
            @SessionAttribute(name = "LOGIN_USER_ID") Long loginId,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Page<FollowerResponse> followerResponses = followQueryService.getFollowers(loginId, pageable);
        return ApiPageResponse.success(followerResponses);
    }

    @DeleteMapping("/followers/{followerId}")
    public ResponseEntity<ApiResponse<Void>> removeFollower(
            @SessionAttribute(name = "LOGIN_USER_ID") Long loginId,
            @PathVariable Long followerId
    ) {
        followCommandService.removeFollower(loginId, followerId);
        return ApiResponse.noContent();
    }
}
