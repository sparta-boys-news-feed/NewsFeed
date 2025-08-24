package com.spartaboys.newsfeed.domain.follow.repository;

import com.spartaboys.newsfeed.domain.follow.entity.Follow;
import com.spartaboys.newsfeed.domain.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowee(User follower, User followee);

    boolean existsByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    Optional<Follow> findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    @Modifying
    @Query("delete from Follow f where f.follower.id = :followerId and f.followee.id = :followeeId")
    void deleteByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    Page<Follow> findAllByFollower(User user, Pageable pageable);

    Page<Follow> findAllByFollowee(User user, Pageable pageable);

    List<Follow> findAllByFollower(User user);

    List<Follow> findAllByFollowee(User user);
}
