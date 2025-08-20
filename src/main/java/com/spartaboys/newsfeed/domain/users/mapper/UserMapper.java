package com.spartaboys.newsfeed.domain.users.mapper;

import com.spartaboys.newsfeed.common.exception.ErrorCode;
import com.spartaboys.newsfeed.domain.users.dto.response.UserPublicResponse;
import com.spartaboys.newsfeed.domain.users.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserPublicResponse toPublicResponse(User user) {
        return UserPublicResponse.of(user.getId(), user.getNickname());
    }
}
