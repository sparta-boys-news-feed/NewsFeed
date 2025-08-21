package com.spartaboys.newsfeed.domain.follow.service.external;

import com.spartaboys.newsfeed.domain.follow.dto.FollowerResponse;
import com.spartaboys.newsfeed.domain.follow.dto.FollowingResponse;
import com.spartaboys.newsfeed.domain.follow.mapper.FollowMapper;
import com.spartaboys.newsfeed.domain.follow.repository.FollowRepository;
import com.spartaboys.newsfeed.domain.users.entity.User;
import com.spartaboys.newsfeed.domain.users.service.UserInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowQueryService {

    private final UserInternalService userInternalService;

    private final FollowRepository followRepository;
    private final FollowMapper followMapper;

    /**
     * 내가 팔로우한 사용자 목록 조회
     */
    public Page<FollowingResponse> getFollowings(Long loginId, Pageable pageable) {
        User user = userInternalService.getUserObjectById(loginId);

        Page<Long> followingIds = followRepository.findAllByFollower(user, pageable)
                .map(follow -> follow.getFollowee().getId());

        List<FollowingResponse> followingResponses = userInternalService.getUserObjectsByIdList(followingIds.getContent()).stream()
                .map(followMapper::toDto)
                .toList();

        return new PageImpl<>(followingResponses, pageable, followingIds.getTotalElements());
    }

    /**
     * 나를 팔로우한 사용자 목록 조회
     */
    public Page<FollowerResponse> getFollowers(Long loginId, Pageable pageable) {
        User user = userInternalService.getUserObjectById(loginId);

        // 자신이 followee 인 목록 -> 나를 팔로우한 사용자 목록
        Page<Long> followerIds = followRepository.findAllByFollowee(user, pageable)
                .map(follow -> follow.getFollower().getId());

        List<FollowerResponse> followerResponses = userInternalService.getUserObjectsByIdList(followerIds.getContent()).stream()
                .map(follower -> {
                    // 상대가 나를 팔로우하고 있는 상태
                    boolean isFollowingMe = followRepository.existsByFollowerIdAndFolloweeId(user.getId(), follower.getId());
                    return followMapper.toDto(follower, isFollowingMe);
                })
                .toList();

        return new PageImpl<>(followerResponses, pageable, followerIds.getTotalElements());
    }
}
