package com.spartaboys.newsfeed.domain.boards.mapper;

import com.spartaboys.newsfeed.domain.boards.dto.request.BoardRequest;
import com.spartaboys.newsfeed.domain.boards.dto.response.BoardResponse;
import com.spartaboys.newsfeed.domain.boards.entity.Board;
import com.spartaboys.newsfeed.domain.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardMapper {

    // 게시글 CRUD에서 BoardRequest Dto -> Board Entity 변한
    public Board toEntity(BoardRequest request, User user) {
        return Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .build();
    }

    // 게시글 CRUD에서 Board Entity -> BoardRequest Dto 변한
    public BoardResponse toDto(Board board) {
        return new BoardResponse(
                board.getId(),
                board.getUser().getId(),
                board.getTitle(),
                board.getContent(),
                board.getLikes(),
                board.getCreatedAt(),
                board.getUpdatedAt(),
                board.getDeletedAt(),
                board.isDeleted());
    }

    ;
}