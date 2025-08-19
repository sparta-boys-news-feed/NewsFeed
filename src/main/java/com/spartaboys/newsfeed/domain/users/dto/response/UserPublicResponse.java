package com.spartaboys.newsfeed.domain.users.dto.response;

public record UserPublicResponse(
        Long id, String nickname
) {
    public static UserPublicResponse of(
            Long id, String nickname
    ) {
        return new UserPublicResponse(id, nickname);
    }

}
