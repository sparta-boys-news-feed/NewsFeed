package com.spartaboys.newsfeed.domain.like.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContentType {
    BOARD("board"),
    COMMENT("comment");

    private final String type;
}
