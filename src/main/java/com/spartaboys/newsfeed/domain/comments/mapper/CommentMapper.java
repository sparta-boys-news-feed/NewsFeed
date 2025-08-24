package com.spartaboys.newsfeed.domain.comments.mapper;

import com.spartaboys.newsfeed.domain.boards.entity.Board;
import com.spartaboys.newsfeed.domain.comments.dto.request.CommentCreateRequest;
import com.spartaboys.newsfeed.domain.comments.dto.response.CommentGetAllResponse;
import com.spartaboys.newsfeed.domain.comments.dto.response.CommentResponse;
import com.spartaboys.newsfeed.domain.comments.entity.Comment;
import com.spartaboys.newsfeed.domain.users.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommentMapper {

    // CommentCreateRequest DTO를 Comment 엔티티로 변환
    public Comment toEntity(CommentCreateRequest request, Board board, User user, Comment parentComment) {
        return Comment.builder()
                .content(request.content())
                .user(user)
                .board(board)
                .parentComment(parentComment)
                .build();
    }

    // 부모 댓글 -> DTO (대댓글 리스트 포함)
    public CommentGetAllResponse toDto(Comment comment, List<Comment> replies) {
        return CommentGetAllResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .nickname(comment.getUser().getNickname())
                .likes(comment.getLikes())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .replies(toDto(replies)) // 대댓글 변환
                .build();
    }

    // 대댓글 & 단일 댓글 -> DTO
    public CommentResponse toDto(Comment reply) {
        return CommentResponse.toDto(
                reply.getId(),
                reply.getContent(),
                reply.getUser().getNickname(),
                reply.getLikes(),
                reply.getCreatedAt(),
                reply.getUpdatedAt());
    }

    // 대댓글 리스트 변환
    public List<CommentResponse> toDto(List<Comment> replies) {
        return replies.stream()
                .map(this::toDto)
                .toList();
    }
}
