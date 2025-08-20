package com.spartaboys.newsfeed.domain.comments.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CommentGetAllResponse {

    private final Long id;
    private final String content;
    private final String nickname;
    private final int likes;
    private final List<CommentResponse> replies;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
