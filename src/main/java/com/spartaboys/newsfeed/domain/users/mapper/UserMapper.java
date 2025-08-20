package com.spartaboys.newsfeed.domain.users.mapper;

import com.spartaboys.newsfeed.common.exception.ErrorCode;
import com.spartaboys.newsfeed.domain.users.dto.response.UserPrivateResponse;
import com.spartaboys.newsfeed.domain.users.dto.response.UserPublicResponse;
import com.spartaboys.newsfeed.domain.users.dto.response.UserUpdateResponse;
import com.spartaboys.newsfeed.domain.users.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserPublicResponse toPublicResponse(User user) {
        return UserPublicResponse.of(user.getId(), user.getNickname());
    }
    public UserPrivateResponse toPrivateResponse(User user) { return UserPrivateResponse.of(user.getId(), user.getEmail(), user.getNickname(), user.getCreatedAt(), user.getModifiedAt()); }
    public UserUpdateResponse toUpdateResponse(User user) { return UserUpdateResponse.of(user.getId(), user.getNickname(), user.getModifiedAt()); }
}
