package com.spartaboys.newsfeed.domain.users.service;

import com.spartaboys.newsfeed.domain.auth.PasswordEncoder;
import com.spartaboys.newsfeed.domain.boards.dto.response.BoardResponse;
import com.spartaboys.newsfeed.domain.boards.service.BoardInternalService;
import com.spartaboys.newsfeed.domain.comments.dto.response.CommentResponse;
import com.spartaboys.newsfeed.domain.comments.entity.Comment;
import com.spartaboys.newsfeed.domain.comments.mapper.CommentMapper;
import com.spartaboys.newsfeed.domain.comments.service.CommentInternalService;
import com.spartaboys.newsfeed.domain.follow.dto.FollowerResponse;
import com.spartaboys.newsfeed.domain.follow.dto.FollowingResponse;
import com.spartaboys.newsfeed.domain.follow.entity.Follow;
import com.spartaboys.newsfeed.domain.follow.mapper.FollowMapper;
import com.spartaboys.newsfeed.domain.follow.service.internal.FollowInternalService;
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
public class UserExternalService {
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final BoardInternalService boardInternalService;
    private final CommentInternalService commentInternalService;
    private final CommentMapper commentMapper;
    private final FollowInternalService followInternalService;
    private final FollowMapper followMapper;

    // 헬퍼 메서드
    private User getUserOrThrow(Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new InvalidUserException(UserErrorCode.USR_INVALID_ID));
    }
    private <T> T getUserMapped(Long id, Function<User, T> mapper) {
        User user = getUserOrThrow(id);

        return mapper.apply(user);
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
        if (!dto.newPassword().equals(dto.newPasswordConfirm())) {
            throw new InvalidUserException(UserErrorCode.USR_PW_CHECK_MISMATCH);
        }

        User currentUser = getUserOrThrow(id);
        if (!passwordEncoder.matches(dto.currentPassword(), currentUser.getPassword())) {
            throw new InvalidUserException(UserErrorCode.USR_PW_CURRENT_MISMATCH);
        }

        currentUser.updatePassword(passwordEncoder.encode(dto.newPassword()));
    }

    public Page<BoardResponse> getBoardsByUserId(Pageable pageable, Long userId) {
        User targetUser = getUserOrThrow(userId);


        return boardInternalService.getBoardsByUserId(pageable, targetUser.getId());
    }

    public Page<CommentResponse> getCommentsByUserId(Pageable pageable, Long userId) {
        User targetUser = getUserOrThrow(userId);

        Page<Comment> comments = commentInternalService.getCommentsByUserId(targetUser.getId(), pageable);

        return comments.map(commentMapper::toDto);
    }

    //  특정 대상이 follow한 사람 목록
    public Page<FollowerResponse> getFollowersFromUserId(Pageable pageable, Long userId) {
        User targetUser = getUserOrThrow(userId);

        // 1. target이 팔로우 중인 user에 대한 Follow 인스턴스 목록 조회
        // 2. 인스턴스 목록에서 followee 필드 따로 분리
        // 3. 분리한 followee(user)와 isFollowing 여부에 따라 FollowerResponse 생성)
        return followInternalService.getFollowersByUserId(targetUser.getId(), pageable)
                .map(Follow::getFollowee)
                .map(user -> followMapper.toDto(user, followInternalService.isFollowing(targetUser.getId(), user.getId())));

    }

    // 불특정 다수가 특정 대상을 follow 했을 때, 불특정 다수의 목록
    public Page<FollowingResponse> getFolloweesFromUserId(Pageable pageable, Long userId) {
        User targetUser = getUserOrThrow(userId);

        // 1. target을 팔로우 중인 user에 대한 Follow 인스턴스 목록 조회
        // 2. 인스턴스 목록에서 follower 필드 따로 분리
        // 3. 분리한 follower(user)로 매핑
        return followInternalService.getFolloweesByUserId(targetUser.getId(), pageable)
                .map(Follow::getFollower)
                .map(followMapper::toDto);

    }

    // public Page<>
}
