package com.spartaboys.newsfeed.domain.users.enums;

import com.spartaboys.newsfeed.domain.like.entity.BoardLike;
import com.spartaboys.newsfeed.domain.like.entity.CommentLike;
import com.spartaboys.newsfeed.domain.like.entity.Like;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum LikeType {
    ALL("all"),
    BOARD("board"),
    COMMENT("comment");

    private final String target;

    public static LikeType from(String targetCode) {
        if (targetCode == null) return ALL;
        String target = targetCode.trim().toLowerCase();
        return switch (target) {
            case "board" -> BOARD;
            case "comment" -> COMMENT;
            default -> ALL; // 나머지는 전부 C
        };
    }

    public Stream<Like> select(List<BoardLike> boards, List<CommentLike> comments) {
        Stream<Like> boardStream   = boards.stream().map(b -> (Like) b);
        Stream<Like> commentStream = comments.stream().map(c -> (Like) c);
        return switch (this) {
            case BOARD   -> boardStream;
            case COMMENT -> commentStream;
            case ALL     -> Stream.concat(boardStream, commentStream);
        };
    }
}
