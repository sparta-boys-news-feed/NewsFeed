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
                .title(request.title())
                .content(request.content())
                .user(user)
                .build();
    }

    // 게시글 CRUD에서 Board Entity -> BoardRequest Dto 변한
    public BoardResponse toDto(Board board) {
        return BoardResponse.builder()
                .id(board.getId())
                .userId(board.getUser().getId())
                .nickname(board.getUser().getNickname())
                .title(board.getTitle())
                .content(board.getContent())
                .likes(board.getLikes())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .deletedAt(board.getDeletedAt())
                .isDeleted(board.isDeleted())
                .build();
    }
}