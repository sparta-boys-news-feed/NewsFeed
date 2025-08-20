package com.spartaboys.newsfeed.domain.like.entity;

import com.spartaboys.newsfeed.domain.board.entity.Board;
import com.spartaboys.newsfeed.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("BOARD")
public class BoardLike extends Like {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    private BoardLike(User user, Board board) {
        super(user);
        this.board = board;
    }

    public static BoardLike create(User user, Board board) {
        return BoardLike.builder()
                .user(user)
                .board(board)
                .build();
    }

}
