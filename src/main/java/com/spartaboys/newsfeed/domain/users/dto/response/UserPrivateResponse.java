package com.spartaboys.newsfeed.domain.users.dto.response;

import java.time.LocalDateTime;

public record UserPrivateResponse(
        Long id, String email, String nickname,
        LocalDateTime createdAt, LocalDateTime updatedAt
) {
    public static UserPrivateResponse toDto(
            Long id, String email, String nickname,
            LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        return new UserPrivateResponse(id, email, nickname, createdAt, updatedAt);
    }
}
