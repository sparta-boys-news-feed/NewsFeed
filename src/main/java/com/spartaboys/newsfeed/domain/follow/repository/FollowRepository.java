package com.spartaboys.newsfeed.domain.follow.repository;

import com.spartaboys.newsfeed.domain.follow.entity.Follow;
import com.spartaboys.newsfeed.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowee(User follower, User followee);

}
