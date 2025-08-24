package com.spartaboys.newsfeed.domain.comments.repository;

import com.spartaboys.newsfeed.domain.comments.entity.Comment;
import com.spartaboys.newsfeed.domain.comments.exception.CommentErrorCode;
import com.spartaboys.newsfeed.domain.comments.exception.InvalidCommentException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 부모 댓글 찾아서 ( ParentComment 가 Null 이며 Deleted가 False 일때 )
    @EntityGraph(attributePaths = "user")
    Page<Comment> findByBoardIdAndParentCommentIsNullAndDeletedIsFalse(Long boardId, Pageable pageable);

    // 대댓글 찾기 ( ParentComment 가 Not Null 이며 Deleted가 False 일때 ) 생성일 기준으로 내림차순
    @EntityGraph(attributePaths = "user")
    List<Comment> findByBoardIdAndParentCommentIsNotNullAndDeletedIsFalseOrderByCreatedAtDesc(Long boardId);

    // 리스트 댓글 랩퍼 메서드 용도 ( 여러 Userid 기준으로 Deleted 가 False 일 때의 모든 댓글  )
    @Query("select c from Comment c where c.user.id in :userIds")
    List<Comment> findAllByUserIdsAndDeletedIsFalse(@Param("userIds") List<Long> userIds);

    // 페이징 댓글 랩퍼 메서드 용도 ( UserId 기준으로 Deleted 가 False 일 때의 모든 댓글 )
    Page<Comment> findAllByUserIdAndDeletedIsFalse(Long userId, Pageable pageable);

    // findById 예외던지기
    default Comment findByIdOrThrowElse (Long commentId) {
        return findById(commentId).orElseThrow(() -> new InvalidCommentException(CommentErrorCode.COMMENT_NOT_FOUND));
    }
}
