package com.spartaboys.newsfeed.domain.boards.entity;

import com.spartaboys.newsfeed.common.entity.BaseEntity;
import com.spartaboys.newsfeed.domain.like.exception.CannotDecreaseLikesException;
import com.spartaboys.newsfeed.domain.like.exception.LikeErrorCode;
import com.spartaboys.newsfeed.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private long likes = 0L;

    @Builder
    public Board(User user, String title, String content, long likes) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.likes = likes;
    }

    public void updateBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }

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
