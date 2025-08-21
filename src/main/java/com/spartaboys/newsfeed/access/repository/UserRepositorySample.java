package com.spartaboys.newsfeed.access.repository;

import com.spartaboys.newsfeed.access.User;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositorySample extends JpaRepository<User,Long> {

    Optional<User> findByLoginId(String loginId);

    boolean existsByLoginId(String loginId);

    void deleteById(Long id);


}
