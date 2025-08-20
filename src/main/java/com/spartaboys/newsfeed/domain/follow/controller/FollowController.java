package com.spartaboys.newsfeed.domain.follow.controller;

import com.spartaboys.newsfeed.common.response.ApiResponse;
import com.spartaboys.newsfeed.domain.follow.service.FollowQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/me")
@RequiredArgsConstructor
public class FollowController {

    private final FollowQueryService followQueryService;

    @PostMapping("/following/{followeeId}")
    public ResponseEntity<ApiResponse<Void>> followUser(
            @SessionAttribute(name = "login_id") Long followerId,
            @PathVariable Long followeeId
    ) {
        followQueryService.followUser(followerId, followeeId);
        return ApiResponse.noContent();
    }
}
