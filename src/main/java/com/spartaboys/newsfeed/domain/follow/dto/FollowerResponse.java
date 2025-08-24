package com.spartaboys.newsfeed.domain.follow.dto;

import lombok.Builder;

/**
 * 나를 팔로우한 사용자 목록 (Followers)
 */
@Builder
public record FollowerResponse(
        Long id,
        String email,
        String nickname,
        boolean isFollowingMe // 상대가 나를 팔로우하고 있는 상태
) {
}
