package com.spartaboys.newsfeed.domain.users.service;

import com.spartaboys.newsfeed.domain.boards.dto.response.BoardResponse;
import com.spartaboys.newsfeed.domain.boards.entity.Board;
import com.spartaboys.newsfeed.domain.boards.mapper.BoardMapper;
import com.spartaboys.newsfeed.domain.boards.service.BoardService;
import com.spartaboys.newsfeed.domain.comments.dto.response.CommentResponse;
import com.spartaboys.newsfeed.domain.comments.entity.Comment;
import com.spartaboys.newsfeed.domain.comments.mapper.CommentMapper;
import com.spartaboys.newsfeed.domain.comments.service.CommentService;
import com.spartaboys.newsfeed.domain.users.dto.request.ChangePasswordRequest;
import com.spartaboys.newsfeed.domain.users.dto.request.UserUpdateRequest;
import com.spartaboys.newsfeed.domain.users.dto.response.UserPrivateResponse;
import com.spartaboys.newsfeed.domain.users.dto.response.UserUpdateResponse;
import com.spartaboys.newsfeed.domain.users.mapper.UserMapper;
import com.spartaboys.newsfeed.domain.users.dto.response.UserPublicResponse;
import com.spartaboys.newsfeed.domain.users.entity.User;
import com.spartaboys.newsfeed.domain.users.exception.InvalidUserException;
import com.spartaboys.newsfeed.domain.users.exception.UserErrorCode;
import com.spartaboys.newsfeed.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final BoardService boardService;
    private final CommentService commentService;
    private final BoardMapper boardMapper;
    private final CommentMapper commentMapper;

    // 헬퍼 메서드
    private User getUserOrThrow(Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new InvalidUserException(UserErrorCode.USR_INVALID_ID));
    }
    private <T> T getUserMapped(Long id, Function<User, T> mapper) {
        User user = getUserOrThrow(id);

        return mapper.apply(user);
    }

    // 외부 서비스 연동용
    public User getUserObjectById(Long userID) {
        return getUserOrThrow(userID);
    }

    // 본 메소드
    public UserPublicResponse getPublicUserById(Long id) {
        return getUserMapped(id, userMapper::toPublicResponse);
    }

    public UserPrivateResponse getPrivateUserById(Long id) {
        return getUserMapped(id, userMapper::toPrivateResponse);
    }

    public List<UserPublicResponse> getPublicUsersByNicknameContaining(String nickname) {
        if (nickname == null || nickname.isEmpty()) {
            throw new InvalidUserException(UserErrorCode.USR_INVALID_NICKNAME);
        }

        return userRepository.findByNicknameContaining(nickname)
                .stream()
                .filter(user -> !user.isDeleted())
                .map(userMapper::toPublicResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserUpdateResponse updateUserProfile(Long id, UserUpdateRequest dto) {
        if (userRepository.existsByNickname(dto.getNickname())) {
            throw new InvalidUserException(UserErrorCode.USR_DUPLICATE_EMAIL);
        }

        User currentUser = getUserOrThrow(id);
        currentUser.updateNickname(dto.getNickname());

        return userMapper.toUpdateResponse(currentUser);
    }

    @Transactional
    public void updateUserPassword(Long id, ChangePasswordRequest dto) {
        // TODO : 비밀번호 암호화 로직 완성되는대로 반영
        if (dto.getNewPasswordConfirm().equals(dto.getNewPassword())) {
            throw new InvalidUserException(UserErrorCode.USR_PW_CHECK_MISMATCH);
        }

        User currentUser = getUserOrThrow(id);
        if (!currentUser.getPassword().equals(dto.getCurrentPassword())) {
            throw new InvalidUserException(UserErrorCode.USR_PW_CURRENT_MISMATCH);
        }

        currentUser.updatePassword(dto.getNewPassword());
    }

    public Page<BoardResponse> getBoardsByUserId(Pageable pageable, Long userId) {
        User targetUser = getUserOrThrow(userId);

        // TODO : 연동 메소드 확인
//        Page<Board> boards = boardService.getBoardsByUserID(pageable, targetUser.getId());
//
//        return boards.map(boardMapper::toDto);
        return null;
    }

    public Page<CommentResponse> getCommentsByUserId(Pageable pageable, Long userId) {
        User targetUser = getUserOrThrow(userId);

        // TODO : 연동 메소드 확인
//        Page<Comment> comments = commentService.getCommentsByUserID(pageable, targetUser.getId());
//
//        return comments.map(commentMapper::toDto);
        return null;
    }

    // public Page<>
}
