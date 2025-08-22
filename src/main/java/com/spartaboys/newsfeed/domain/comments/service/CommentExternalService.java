package com.spartaboys.newsfeed.domain.comments.service;

import com.spartaboys.newsfeed.domain.boards.entity.Board;
import com.spartaboys.newsfeed.domain.boards.service.BoardInternalService;
import com.spartaboys.newsfeed.domain.comments.dto.request.CommentCreateRequest;
import com.spartaboys.newsfeed.domain.comments.dto.request.CommentUpdateRequest;
import com.spartaboys.newsfeed.domain.comments.dto.response.CommentGetAllResponse;
import com.spartaboys.newsfeed.domain.comments.dto.response.CommentResponse;
import com.spartaboys.newsfeed.domain.comments.entity.Comment;
import com.spartaboys.newsfeed.domain.comments.mapper.CommentMapper;
import com.spartaboys.newsfeed.domain.comments.repository.CommentRepository;
import com.spartaboys.newsfeed.domain.users.entity.User;
import com.spartaboys.newsfeed.domain.users.service.UserInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentExternalService {

    private final BoardInternalService boardInternalService;
    private final UserInternalService userInternalService;

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    // 댓글 생성
    @Transactional
    public CommentResponse createComment(Long boardId, Long commentId, Long loginUserId, CommentCreateRequest request) {

        // 게시글 및 유저 찾아오기
        Board findBoard = boardInternalService.getBoardById(boardId);
        User loginUser = userInternalService.getUserObjectById(loginUserId);

        // 댓글 생성
        Comment comment;
        if (commentId == null) {
            // 일반 댓글
            comment = commentMapper.toEntity(request, findBoard, loginUser, null);
        } else {
            // 대댓글 (부모 댓글 조회 후 삭제 및 부모 댓글 여부 체크)
            Comment parentComment = commentRepository.findByIdOrThrowElse(commentId);
            parentComment.validateCommentNotDeleted();
            parentComment.validateHaveReply();
            comment = commentMapper.toEntity(request, findBoard, loginUser, parentComment);
        }

        // 댓글 저장
        commentRepository.save(comment);

        // 댓글 DTO 변환 및 반환
        return commentMapper.toDto(comment);
    }

    // 댓글 전체 조회 (해당 게시글)
    @Transactional (readOnly = true)
    public Page<CommentGetAllResponse> getAllByBoardId(Long boardId, Pageable pageable) {

        // 게시글 검증 ( 객체 생성 X )
        boardInternalService.getBoardById(boardId);

        // 일반 댓글만 페이징
        Page<Comment> pageComments = commentRepository.findByBoardIdAndParentCommentIsNullAndDeletedIsFalse(boardId, pageable);

        // 해당 게시물의 대댓글 모두 조회
        List<Comment> allReplies = commentRepository.findByBoardIdAndParentCommentIsNotNullAndDeletedIsFalseOrderByCreatedAtDesc(boardId);

        // 부모 댓글 ID 기준으로 대댓글 그룹화
        Map<Long, List<Comment>> repliesMap = allReplies.stream()
                .collect(Collectors.groupingBy(reply -> reply.getParentComment().getId()));

        // 부모 댓글 + 대댓글 매핑
        return pageComments.map(parent ->
                commentMapper.toDto(parent, repliesMap.getOrDefault(parent.getId(), List.of()))
        );
    }

    // 댓글 단일 조회
    @Transactional (readOnly = true)
    public CommentResponse getComment(Long boardId, Long commentId) {

        // 댓글 찾기 & 삭제 여부 & 게시글 ID 동일한지 검증
        Comment findComment = checkComment(commentId, boardId);

        // 해당 댓글 반환
        return commentMapper.toDto(findComment);
    }

    // 댓글 수정
    @Transactional
    public CommentResponse updateComment(Long boardId, Long commentId, Long loginUserId, CommentUpdateRequest request) {

        // 댓글 찾기 & 삭제 여부 & 게시글 ID 동일한지 검증
        Comment findComment = checkComment(commentId, boardId);

        // 유저의 권한 확인
        findComment.validateOwner(loginUserId);

        // 댓글 내용 수정
        findComment.updateContent(request.content());

        // 수정된 내용 반환
        return commentMapper.toDto(findComment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long boardId, Long commentId, Long loginUserId) {

        // 댓글 찾기 & 삭제 여부 & 게시글 ID 동일한지 검증
        Comment findComment = checkComment(commentId, boardId);

        // 유저의 권한 확인
        findComment.validateOwner(loginUserId);

        // 해당 댓글 삭제 ( isDelete = true, deletedAt = LocalDateTime.now() )
        findComment.delete();
    }






    // ===== 헬퍼 메서드 =====

    // 댓글 찾기 & 삭제 여부 & 게시글 ID 동일한지 검증
    private Comment checkComment(Long commentId, Long boardId) {
        // 해당 댓글 찾기
        Comment findComment = commentRepository.findByIdOrThrowElse(commentId);

        // 해당 댓글이 삭제 되었는지 확인
        findComment.validateCommentNotDeleted();

        // 해당 게시글이 댓글 게시글 ID와 동일한지 확인
        findComment.validateBoard(boardInternalService.getBoardById(boardId));

        return findComment;
    }
}
