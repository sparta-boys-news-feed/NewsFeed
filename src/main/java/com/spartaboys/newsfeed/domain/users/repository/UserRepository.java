package com.spartaboys.newsfeed.domain.users.repository;

import com.spartaboys.newsfeed.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String username);
    List<User> findByNicknameContaining(String nickname);

    boolean existsByEmail(String email);
    boolean existsByNickname(String username);
}
