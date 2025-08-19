package com.spartaboys.newsfeed.domain.comments.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentUpdateRequest {

    @NotBlank(message = "댓글 내용은 필수입니다.")
    @Size(min = 2, max = 100, message = "댓글 내용은 2자 이상 100자 이하입니다.")
    private final String content;

    public CommentUpdateRequest(String content) {
        this.content = content;
    }
}
