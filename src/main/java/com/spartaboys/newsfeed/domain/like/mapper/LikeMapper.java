package com.spartaboys.newsfeed.domain.like.mapper;

import com.spartaboys.newsfeed.domain.like.dto.ContentType;
import com.spartaboys.newsfeed.domain.like.dto.LikeResponse;
import com.spartaboys.newsfeed.domain.like.entity.BoardLike;
import com.spartaboys.newsfeed.domain.like.entity.Like;
import org.springframework.stereotype.Component;

@Component
public class LikeMapper {
    public LikeResponse toLikeResponse(Like like) {
        return LikeResponse.toDto(like.getContentId(), like.getContentType());
    }
}
