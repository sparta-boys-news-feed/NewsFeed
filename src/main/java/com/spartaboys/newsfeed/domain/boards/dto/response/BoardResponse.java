package com.spartaboys.newsfeed.domain.boards.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BoardResponse {
    private final Long Id;
    private final Long userId;
    private final String title;
    private final String content;
    private final Long likes;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime deletedAt;
    private final boolean isDeleted;
}
