package com.spartaboys.newsfeed.domain.like.comments.service.internal;

import com.spartaboys.newsfeed.domain.like.comments.repository.CommentLikeRepository;
import com.spartaboys.newsfeed.domain.like.entity.CommentLike;
import com.spartaboys.newsfeed.domain.users.entity.User;
import com.spartaboys.newsfeed.domain.users.service.UserInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentLikeInternalService {

    private final UserInternalService userInternalService;
    private final CommentLikeRepository commentLikeRepository;

    public List<CommentLike> getCommentLikesByUserId(Long userId) {
        User user = userInternalService.getUserObjectById(userId);
        return commentLikeRepository.findAllByUser(user);
    }

    public Page<CommentLike> getCommentLikesByUserId(Long userId, Pageable pageable) {
        User user = userInternalService.getUserObjectById(userId);
        return commentLikeRepository.findAllByUser(user, pageable);
    }
}
