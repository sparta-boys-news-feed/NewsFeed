package com.spartaboys.newsfeed.domain.follow.dto;

import lombok.Builder;

/**
 * 내가 팔로우한 사용자 목록 (Followings)
 */
@Builder
public record FollowingResponse(
        Long id,
        String email,
        String nickname
) {
}
