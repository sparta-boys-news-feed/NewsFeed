package com.spartaboys.newsfeed.domain.like.dto;

// TODO : (가능하면) Like한 컨텐츠의 내용도 포함하도록 개선
public record LikeResponse (Long id, String type){
    public static LikeResponse toDto(
            Long id, ContentType contentType
    ) {
        return new LikeResponse(id, contentType.toString());
    }
}
