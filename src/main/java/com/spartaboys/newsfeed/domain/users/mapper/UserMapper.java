package com.spartaboys.newsfeed.domain.users.mapper;

import com.spartaboys.newsfeed.domain.users.dto.response.UserPrivateResponse;
import com.spartaboys.newsfeed.domain.users.dto.response.UserPublicResponse;
import com.spartaboys.newsfeed.domain.users.dto.response.UserUpdateResponse;
import com.spartaboys.newsfeed.domain.users.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserPublicResponse toPublicResponse(User user) {
        return UserPublicResponse.toDto(user.getId(), user.getNickname());
    }
    public UserPrivateResponse toPrivateResponse(User user) { return UserPrivateResponse.toDto(user.getId(), user.getEmail(), user.getNickname(), user.getCreatedAt(), user.getUpdatedAt()); }
    public UserUpdateResponse toUpdateResponse(User user) { return UserUpdateResponse.toDto(user.getId(), user.getNickname(), user.getUpdatedAt()); }
}
