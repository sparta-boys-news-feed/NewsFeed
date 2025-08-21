package com.spartaboys.newsfeed.domain.comments.service;

import com.spartaboys.newsfeed.domain.comments.entity.Comment;
import com.spartaboys.newsfeed.domain.comments.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentInternalService {

    private final CommentRepository commentRepository;

    // 단일 댓글 조회용 랩퍼 ( Id에 해당하는 댓글이 없을 경우 NOT_FOUND )
    public Comment getCommentById(Long commentId) {

        // 단일 댓글 조회 ( Board, User 포함 )
        Comment findComment = commentRepository.findByIdOrThrowElse(commentId);

        // 해당 댓글이 삭제 되었는지 확인
        findComment.validateCommentNotDeleted();

        return findComment;
    }

    // 유저 ID를 기준으로 찾은 댓글 페이징 랩퍼 ( 삭제된 댓글 제외 )
    public Page<Comment> getCommentsByUserId(Long userId, Pageable pageable) {
        return commentRepository.findAllByUserIdAndDeletedIsFalse(userId, pageable);
    }

    // 여러 유저 ID를 기준으로 찾은 댓글 리스트 랩퍼 ( 삭제된 댓글 제외 )
    public List<Comment> getCommentsByUserIds(List<Long> userIds) {
        return commentRepository.findAllByUserIdsAndDeletedIsFalse(userIds);
    }
}
