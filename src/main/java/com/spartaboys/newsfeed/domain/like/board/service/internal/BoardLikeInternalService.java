package com.spartaboys.newsfeed.domain.like.board.service.internal;

import com.spartaboys.newsfeed.domain.like.board.repository.BoardLikeRepository;
import com.spartaboys.newsfeed.domain.like.entity.BoardLike;
import com.spartaboys.newsfeed.domain.users.entity.User;
import com.spartaboys.newsfeed.domain.users.service.UserInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardLikeInternalService {

    private final UserInternalService userInternalService;
    private final BoardLikeRepository boardLikeRepository;

    public List<BoardLike> getBoardLikesByUserId(Long userId) {
        User user = userInternalService.getUserObjectById(userId);
        return boardLikeRepository.findAllByUser(user);
    }

    public Page<BoardLike> getBoardLikesByUserId(Long userId, Pageable pageable) {
        User user = userInternalService.getUserObjectById(userId);
        return boardLikeRepository.findAllByUser(user, pageable);
    }

}
