package com.spartaboys.newsfeed.domain.like.comments.service;

import com.spartaboys.newsfeed.domain.boards.entity.Board;
import com.spartaboys.newsfeed.domain.boards.service.BoardService;
import com.spartaboys.newsfeed.domain.comments.entity.Comment;
import com.spartaboys.newsfeed.domain.comments.exception.CommentErrorCode;
import com.spartaboys.newsfeed.domain.comments.service.CommentService;
import com.spartaboys.newsfeed.domain.like.comments.exception.CommentLikeErrorCode;
import com.spartaboys.newsfeed.domain.like.comments.exception.NotCommentOfBoardException;
import com.spartaboys.newsfeed.domain.like.comments.repository.CommentLikeRepository;
import com.spartaboys.newsfeed.domain.like.entity.CommentLike;
import com.spartaboys.newsfeed.domain.like.exception.AlreadyLikedException;
import com.spartaboys.newsfeed.domain.like.exception.LikeAccessDeniedException;
import com.spartaboys.newsfeed.domain.like.exception.LikeErrorCode;
import com.spartaboys.newsfeed.domain.like.exception.LikeNotFoundException;
import com.spartaboys.newsfeed.domain.users.entity.User;
import com.spartaboys.newsfeed.domain.users.service.UserInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentLikeCommandService {

    private final UserInternalService userInternalService;
    private final BoardService boardService;
    private final CommentService commentService;

    private final CommentLikeRepository commentLikeRepository;

    /**
     * 댓글 좋아요 등록 메서드
     *
     * <p>주어진 사용자 ID(loginId), 게시글 ID(boardId), 댓글 ID(commentId)를 기반으로
     * 해당 사용자가 누른 댓글 좋아요를 등록한다.</p>
     *
     * <ul>
     *   <li>댓글이 해당 게시글에 속하지 않으면 {@link NotCommentOfBoardException} 발생</li>
     *   <li>이미 좋아요를 누른 댓글이면 {@link AlreadyLikedException} 발생</li>
     * </ul>
     *
     * <p>좋아요가 정상적으로 등록되면, 댓글의 좋아요 수가 증가한다.</p>
     *
     * @param loginId   좋아요를 누른 사용자 ID
     * @param boardId   댓글이 속한 게시글 ID
     * @param commentId 좋아요를 등록할 댓글 ID
     * @throws NotCommentOfBoardException 댓글이 게시글에 속하지 않은 경우
     * @throws AlreadyLikedException      사용자가 이미 좋아요를 누른 경우
     */
    public void likeComment(Long loginId, Long boardId, Long commentId) {
        User user = userInternalService.getUserObjectById(loginId);
        Board board = boardService.getBoardById(boardId);
        Comment comment = commentService.getCommentById(commentId);

        // 현재 댓글이 게시글 소유인지 검증
        if (!comment.isOwnedBy(board)) {
            throw new NotCommentOfBoardException(CommentErrorCode.NOT_COMMENT_OF_BOARD);
        }

        // 좋아요 중복 방지
        if (commentLikeRepository.existsByUserAndComment(user, comment)) {
            throw new AlreadyLikedException(CommentLikeErrorCode.ALREADY_LIKED_COMMENT);
        }

        // 댓글 좋아요 등록
        commentLikeRepository.save(
                CommentLike.create(user, comment)
        );

        // 댓글 좋아요 수 증가
        comment.increaseLikes();
    }

    /**
     * 댓글 좋아요 취소 메서드
     *
     * <p>주어진 사용자 ID(loginId), 게시글 ID(boardId), 댓글 ID(commentId)를 기반으로
     * 해당 사용자가 누른 댓글 좋아요를 삭제한다.</p>
     *
     * <ul>
     *   <li>댓글이 게시글에 속하지 않으면 {@link NotCommentOfBoardException} 발생</li>
     *   <li>좋아요가 존재하지 않으면 {@link LikeNotFoundException} 발생</li>
     *   <li>좋아요 작성자가 아닌 사용자가 삭제를 시도하면 {@link LikeAccessDeniedException} 발생</li>
     * </ul>
     *
     * <p>좋아요가 정상적으로 삭제되면, 댓글의 좋아요 수가 감소한다.</p>
     *
     * @param loginId   좋아요를 누른 사용자 ID
     * @param boardId   댓글이 속한 게시글 ID
     * @param commentId 삭제할 댓글 좋아요 ID
     * @throws NotCommentOfBoardException 댓글이 게시글에 속하지 않은 경우
     * @throws LikeNotFoundException      삭제할 댓글 좋아요가 존재하지 않는 경우
     * @throws LikeAccessDeniedException  좋아요 작성자가 아닌 사용자가 삭제를 시도한 경우
     */
    public void unlikeComment(Long loginId, Long boardId, Long commentId) {
        User user = userInternalService.getUserObjectById(loginId);
        Board board = boardService.getBoardById(boardId);
        Comment comment = commentService.getCommentById(commentId);

        // 사용자가 해당 댓글에 남긴 좋아요 조회
        CommentLike commentLike = commentLikeRepository.findByUserAndComment(user, comment)
                .orElseThrow(() -> new LikeNotFoundException(LikeErrorCode.LIKE_NOT_FOUND));

        // 댓글이 해당 게시글에 속하는지 검증
        if (!comment.isOwnedBy(board)) {
            throw new NotCommentOfBoardException(CommentErrorCode.NOT_COMMENT_OF_BOARD);
        }

        // 좋아요 삭제 권한 체크 (좋아요를 누른 본인만 삭제 가능)
        if (!commentLike.isOwnerBy(user)) {
            throw new LikeAccessDeniedException(LikeErrorCode.NOT_LIKE_OWNER);
        }

        // 댓글 좋아요 삭제
        commentLikeRepository.delete(commentLike);

        // 댓글 좋아요 수 감소
        comment.decreaseLikes();
    }
}
