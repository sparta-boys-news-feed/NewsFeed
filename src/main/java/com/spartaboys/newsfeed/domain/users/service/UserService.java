package com.spartaboys.newsfeed.domain.users.service;

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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    private User getUserOrThrow(Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new InvalidUserException(UserErrorCode.USR_INVALID_ID));
    }
    private <T> T getUserMapped(Long id, Function<User, T> mapper) {
        User user = getUserOrThrow(id);

        return mapper.apply(user);
    }

    public UserPublicResponse getPublicUserById(Long id) {
        return getUserMapped(id, userMapper::toPublicResponse);
    }

    public UserPrivateResponse getPrivateUserById(Long id) {
        return getUserMapped(id, userMapper::toPrivateResponse);
    }

    public UserUpdateResponse updateUserProfile(Long id, UserUpdateRequest dto) {
        if (userRepository.existsByNickname(dto.getNickname())) {
            throw new InvalidUserException(UserErrorCode.USR_DUPLICATE_EMAIL);
        }

        User currentUser = getUserOrThrow(id);
        currentUser.updateNickname(dto.getNickname());

        return userMapper.toUpdateResponse(currentUser);
    }

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

}
