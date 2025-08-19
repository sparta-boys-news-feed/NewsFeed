package com.spartaboys.newsfeed.domain.comments.repository;

import com.spartaboys.newsfeed.domain.comments.entity.Comment;
import com.spartaboys.newsfeed.domain.comments.exception.CommentErrorCode;
import com.spartaboys.newsfeed.domain.comments.exception.InvalidCommentException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = "user")
    Page<Comment> findAllByBoardIdOrderByCreatedAtDesc(Long boardId, Pageable pageable);

    default Comment findByIdOrThrowElse (Long commentId) {
        return findById(commentId).orElseThrow(() -> new InvalidCommentException(CommentErrorCode.COMMENT_NOT_FOUND));
    }
}
