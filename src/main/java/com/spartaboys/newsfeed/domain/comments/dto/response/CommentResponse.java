package com.spartaboys.newsfeed.domain.comments.dto.response;

import java.time.LocalDateTime;

public record CommentResponse(Long id, String content, String nickname, int likes, LocalDateTime createdAt, LocalDateTime updatedAt) {

    public static CommentResponse toDto(Long id, String content, String nickname, int likes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new CommentResponse(id, content, nickname, likes, createdAt, updatedAt);
    }
}
