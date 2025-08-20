package com.spartaboys.newsfeed.domain.comments.entity;

import com.spartaboys.newsfeed.common.entity.BaseEntity;
import com.spartaboys.newsfeed.domain.boards.entity.Board;
import com.spartaboys.newsfeed.domain.comments.exception.CommentErrorCode;
import com.spartaboys.newsfeed.domain.comments.exception.InvalidCommentException;
import com.spartaboys.newsfeed.domain.like.exception.CannotDecreaseLikesException;
import com.spartaboys.newsfeed.domain.like.exception.LikeErrorCode;
import com.spartaboys.newsfeed.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(nullable = false)
    private String content;

    private int likes = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @Builder
    public Comment(String content, Board board, User user) {
        this.content = content;
        this.board = board;
        this.user = user;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void validateCommentNotDeleted() {
        if (isDeleted()) {
            throw new InvalidCommentException(CommentErrorCode.COMMENT_NOT_FOUND);
        }
    }

    public void validateBoardNotDeleted() {
        if (board.isDeleted()) {
            throw new InvalidCommentException(CommentErrorCode.BOARD_NOT_FOUND);
        }
    }

    public void validateBoard(Board board) {
        if (!board.getId().equals(this.board.getId())) {
            throw new InvalidCommentException(CommentErrorCode.BOARD_BAD_REQUEST);
        }
    }

    public void validateOwner(Long userId) {
        if (!this.user.getId().equals(userId)) {
            throw new InvalidCommentException(CommentErrorCode.UNAUTHORIZED_COMMENT_ACCESS);
        }
    }

    public boolean isOwnedBy(Board board) {
        return ObjectUtils.nullSafeEquals(this.board, board);
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
