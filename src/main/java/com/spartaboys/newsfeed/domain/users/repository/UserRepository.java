package com.spartaboys.newsfeed.domain.users.repository;

import com.spartaboys.newsfeed.domain.users.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String username);
    List<User> findByNicknameContaining(String nickname);

    boolean existsByEmail(String email);
    boolean existsByNickname(String username);
}
