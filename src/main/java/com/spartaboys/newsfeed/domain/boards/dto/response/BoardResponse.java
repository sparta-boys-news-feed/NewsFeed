package com.spartaboys.newsfeed.domain.boards.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BoardResponse(Long Id, Long userId, String title, String content, Long likes, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt, boolean isDeleted) {
}
