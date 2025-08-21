package com.spartaboys.newsfeed.domain.users.service;

import com.spartaboys.newsfeed.domain.users.entity.User;
import com.spartaboys.newsfeed.domain.users.exception.InvalidUserException;
import com.spartaboys.newsfeed.domain.users.exception.UserErrorCode;
import com.spartaboys.newsfeed.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserInternalService {
    private final UserRepository userRepository;

    public User getUserObjectById(Long userID) {
        return userRepository.findByIdAndDeletedIsFalse(userID)
                .orElseThrow(() -> new InvalidUserException(UserErrorCode.USR_INVALID_ID));
    }

    public List<User> getUserObjectsByIdList(List<Long> userIDs) {
        return userRepository.findAllByIdInAndDeletedIsFalse(userIDs);
    }

    // TODO : 필요성 확인 및 제거 검토
    public boolean isUserValid(Long userID) {
        return userRepository.existsByIdAndDeletedIsFalse(userID);
    }
}
