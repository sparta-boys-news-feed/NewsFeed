package com.spartaboys.newsfeed.domain.comments.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CommentResponse {

    private final Long id;
    private final String content;
    private final String nickname;
    private final int likes;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
