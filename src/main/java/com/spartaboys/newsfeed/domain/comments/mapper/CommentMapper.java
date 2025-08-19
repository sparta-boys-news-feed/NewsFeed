package com.spartaboys.newsfeed.domain.comments.mapper;

import com.spartaboys.newsfeed.domain.boards.entity.Board;
import com.spartaboys.newsfeed.domain.comments.dto.request.CommentCreateRequest;
import com.spartaboys.newsfeed.domain.comments.dto.response.CommentResponse;
import com.spartaboys.newsfeed.domain.comments.entity.Comment;
import com.spartaboys.newsfeed.domain.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    // CommentCreateRequest DTO를 Comment 엔티티로 변환
    public Comment toEntity(CommentCreateRequest request, Board board, User user) {
        return Comment.builder()
                .content(request.getContent())
                .user(user)
                .board(board)
                .build();
    }

    // Comment 엔티티를 DTO로 변환
    public CommentResponse toDto(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getNickname(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }

    // 댓글 페이징 각 댓글을 DTO로 변환
    public Page<CommentResponse> toDto(Page<Comment> comments) {
        return comments.map(this::toDto);
    }
}
