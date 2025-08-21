package com.spartaboys.newsfeed.domain.follow.repository;

import com.spartaboys.newsfeed.domain.follow.entity.Follow;
import com.spartaboys.newsfeed.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowee(User follower, User followee);

    @Modifying
    @Query("delete from Follow f where f.follower.id = :follower and f.followee.id = :folloeweeId")
    void deleteByFollowerIdAndFolloweeId(Long followerId, Long followeeId);
}
