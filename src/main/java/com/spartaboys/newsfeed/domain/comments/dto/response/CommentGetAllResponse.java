package com.spartaboys.newsfeed.domain.comments.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CommentGetAllResponse {

    private final Long id;
    private final String content;
    private final String nickname;
    private final int likes;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<CommentResponse> replies;
}
