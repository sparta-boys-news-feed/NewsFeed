package com.spartaboys.newsfeed.domain.comments.repository;

import com.spartaboys.newsfeed.domain.comments.entity.Comment;
import com.spartaboys.newsfeed.domain.comments.exception.CommentErrorCode;
import com.spartaboys.newsfeed.domain.comments.exception.InvalidCommentException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 부모 댓글 찾아서 ( ParentComment 가 Null ) 생성일 기준으로 내림차순
    @EntityGraph(attributePaths = "user")
    Page<Comment> findByBoardIdAndParentCommentIsNullOrderByCreatedAtDesc(Long boardId, Pageable pageable);

    // 대댓글 찾기 ( ParentComment 가 Not Null )
    @EntityGraph(attributePaths = "user")
    List<Comment> findByBoardIdAndParentCommentIsNotNull(Long boardId);

    default Comment findByIdOrThrowElse (Long commentId) {
        return findById(commentId).orElseThrow(() -> new InvalidCommentException(CommentErrorCode.COMMENT_NOT_FOUND));
    }
}
