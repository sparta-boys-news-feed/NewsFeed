package com.spartaboys.newsfeed.domain.comments.service;

import com.spartaboys.newsfeed.domain.boards.entity.Board;
import com.spartaboys.newsfeed.domain.boards.repository.BoardRepository;
import com.spartaboys.newsfeed.domain.comments.dto.request.CommentCreateRequest;
import com.spartaboys.newsfeed.domain.comments.dto.request.CommentUpdateRequest;
import com.spartaboys.newsfeed.domain.comments.dto.response.CommentResponse;
import com.spartaboys.newsfeed.domain.comments.entity.Comment;
import com.spartaboys.newsfeed.domain.comments.exception.*;
import com.spartaboys.newsfeed.domain.comments.mapper.CommentMapper;
import com.spartaboys.newsfeed.domain.comments.repository.CommentRepository;
import com.spartaboys.newsfeed.domain.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final CommentMapper commentMapper;

    // 댓글 생성
    @Transactional
    public CommentResponse createComment(Long boardId, User loginUser, CommentCreateRequest request) {

        // 게시글 찾아오기
        Board findBoard = boardRepository.findById(boardId).orElseThrow(() -> new InvalidCommentException(CommentErrorCode.BOARD_NOT_FOUND));

        // 댓글 생성
        Comment comment = commentMapper.toEntity(request, findBoard, loginUser);

        // 댓글 저장
        commentRepository.save(comment);

        // 댓글 DTO 변환 및 반환
        return commentMapper.toDto(comment);
    }

    // 댓글 전체 조회 (해당 게시글)
    @Transactional (readOnly = true)
    public Page<CommentResponse> getAllByBoardId(Long boardId, Pageable pageable) {

        // 게시글 찾기
        Board findBoard = boardRepository.findById(boardId).orElseThrow(() -> new InvalidCommentException(CommentErrorCode.BOARD_NOT_FOUND));

        // 해당 게시글이 삭제 되었는지 확인
        checkBoardIsDelete(findBoard);

        // 해당 게시글의 댓글들 가져와서 페이징 조회
        Page<Comment> pageComments = commentRepository.findAllByBoardIdOrderByCreatedAtDesc(boardId, pageable);

        // 페이지 DTO 변환 및 반환
        return commentMapper.toDto(pageComments);
    }

    // 댓글 단일 조회
    @Transactional (readOnly = true)
    public CommentResponse getComment(Long boardId, Long commentId) {

        // 해당 댓글 찾기
        Comment findComment = commentRepository.findByIdOrThrowElse(commentId);

        // 해당 게시글이 삭제 되었는지 확인
        findComment.validNotDeleteBoard();

        // 해당 게시글이 댓글 게시글 ID와 동일한지 확인
        checkBoardId(findComment, boardId);

        // 해당 댓글이 삭제 되었는지 확인
        findComment.validNotDeleteComment();

        // 해당 댓글 반환
        return commentMapper.toDto(findComment);
    }

    // 댓글 수정
    @Transactional
    public CommentResponse updateComment(Long boardId, Long commentId, User loginUser, CommentUpdateRequest request) {

        // 해당 댓글 찾기
        Comment findComment = commentRepository.findByIdOrThrowElse(commentId);

        // 해당 게시글이 삭제 되었는지 확인
        findComment.validNotDeleteBoard();

        // 해당 게시글이 댓글 게시글 ID와 동일한지 확인
        checkBoardId(findComment, boardId);

        // 해당 댓글이 삭제 되었는지 확인
        findComment.validNotDeleteComment();

        // 유저의 권한 확인
        checkPermission(findComment, loginUser.getId());

        // 댓글 내용 수정
        findComment.updateContent(request.getContent());

        // 수정된 내용 반환
        return commentMapper.toDto(findComment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long boardId, Long commentId, User loginUser) {

        // 해당 댓글 찾기
        Comment findComment = commentRepository.findByIdOrThrowElse(commentId);

        // 해당 게시글이 삭제 되었는지 확인
        findComment.validNotDeleteBoard();

        // 해당 게시글이 댓글 게시글 ID와 동일한지 확인
        checkBoardId(findComment, boardId);

        // 해당 댓글이 삭제 되었는지 확인
        findComment.validNotDeleteComment();

        // 유저의 권한 확인
        checkPermission(findComment, loginUser.getId());

        // 해당 댓글 삭제 ( isDelete = true, deletedAt = LocalDateTime.now() )
        findComment.delete();
    }






    // ===== 헬퍼 메서드 =====

    // 유저 권한 확인
    private void checkPermission(Comment comment, Long loginUserId) {
        if (!comment.getUser().getId().equals(loginUserId)) {
            throw new InvalidCommentException(CommentErrorCode.UNAUTHORIZED_COMMENT_ACCESS);
        }
    }

    // 게시글 삭제 여부
    private void checkBoardIsDelete(Board board) {
        if (board.isDeleted()) {
            throw new InvalidCommentException(CommentErrorCode.BOARD_NOT_FOUND);
        }
    }

    private void checkBoardId(Comment comment, Long boardId) {
        if (!comment.getBoard().getId().equals(boardId)) {
            throw new InvalidCommentException(CommentErrorCode.BOARD_BAD_REQUEST);
        }
    }
}
