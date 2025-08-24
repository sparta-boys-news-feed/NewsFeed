package com.spartaboys.newsfeed.domain.users.dto.response;

import java.time.LocalDateTime;

public record UserUpdateResponse(
        Long id, String nickname,
        LocalDateTime updatedAt
) {
    public static UserUpdateResponse toDto(
            Long id, String nickname,
            LocalDateTime updatedAt
    ) {
        return new UserUpdateResponse(id, nickname, updatedAt);
    }
}
