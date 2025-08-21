package com.spartaboys.newsfeed.domain.follow.service.internal;

import com.spartaboys.newsfeed.domain.follow.entity.Follow;
import com.spartaboys.newsfeed.domain.follow.exception.FollowErrorCode;
import com.spartaboys.newsfeed.domain.follow.exception.FollowNotFoundException;
import com.spartaboys.newsfeed.domain.follow.repository.FollowRepository;
import com.spartaboys.newsfeed.domain.users.entity.User;
import com.spartaboys.newsfeed.domain.users.service.UserInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowInternalService {

    private final UserInternalService userInternalService;

    private final FollowRepository followRepository;

    public Follow getFollowBy(Long followerId, Long followeeId) {
        return followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId)
                .orElseThrow(() -> new FollowNotFoundException(FollowErrorCode.FOLLOW_NOT_FOUND));
    }

    public boolean isFollowing(Long followerId, Long followeeId) {
        return followRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId);
    }

    /**
     * 팔로워 : 나를 친구로 추가한 사람
     *
     * @param userId 조회하려는 사용자 ID
     * @return List 팔로워 목록
     */
    public List<Follow> getFollowersByUserId(Long userId) {
        User user = userInternalService.getUserObjectById(userId);
        return followRepository.findAllByFollower(user);
    }

    /**
     * 팔로워 : 나를 친구로 추가한 사람
     *
     * @param userId 조회하려는 사용자 ID
     * @return Page 팔로워 목록
     */
    public Page<Follow> getFollowersByUserId(Long userId, Pageable pageable) {
        User user = userInternalService.getUserObjectById(userId);
        return followRepository.findAllByFollower(user, pageable);
    }

    /**
     * 팔로잉 : 내가 친구로 추가한 사람
     *
     * @param userId 조회하려는 사용자 ID
     * @return List 팔로잉 목록
     */
    public List<Follow> getFolloweesByUserId(Long userId) {
        User user = userInternalService.getUserObjectById(userId);
        return followRepository.findAllByFollowee(user);
    }

    /**
     * 팔로잉 : 내가 친구로 추가한 사람
     *
     * @param userId 조회하려는 사용자 ID
     * @return Page 팔로잉 목록
     */
    public Page<Follow> getFolloweesByUserId(Long userId, Pageable pageable) {
        User user = userInternalService.getUserObjectById(userId);
        return followRepository.findAllByFollowee(user, pageable);
    }
}
