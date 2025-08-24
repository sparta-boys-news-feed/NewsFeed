package com.spartaboys.newsfeed.domain.follow.mapper;

import com.spartaboys.newsfeed.domain.follow.dto.FollowerResponse;
import com.spartaboys.newsfeed.domain.follow.dto.FollowingResponse;
import com.spartaboys.newsfeed.domain.users.entity.User;
import org.springframework.stereotype.Component;

@Component
public class FollowMapper {

    public FollowingResponse toDto(User user) {
        return FollowingResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }

    public FollowerResponse toDto(User user, boolean isFollowingMe) {
        return FollowerResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .isFollowingMe(isFollowingMe)
                .build();

    }
}
