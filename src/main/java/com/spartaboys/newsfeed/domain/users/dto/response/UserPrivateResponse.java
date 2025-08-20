package com.spartaboys.newsfeed.domain.users.dto.response;

import java.time.LocalDateTime;

public record UserPrivateResponse(
        Long id, String email, String nickname,
        LocalDateTime createdAt, LocalDateTime modifiedAt
) {
    public static UserPrivateResponse of(
            Long id, String email, String nickname,
            LocalDateTime createdAt, LocalDateTime modifiedAt
    ) {
        return new UserPrivateResponse(id, email, nickname, createdAt, modifiedAt);
    }
}
