package com.spartaboys.newsfeed.domain.board.entity;

import com.spartaboys.newsfeed.domain.User;
import com.spartaboys.newsfeed.domain.like.exception.CannotDecreaseLikesException;
import com.spartaboys.newsfeed.domain.like.exception.LikeErrorCode;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private int likes;

    public void increaseLikes() {
        likes++;
    }

    public void decreaseLikes() {
        if (likes <= 0) {
            throw new CannotDecreaseLikesException(LikeErrorCode.CANNOT_DECREASE_LIKES);
        }
        likes--;
    }

}
