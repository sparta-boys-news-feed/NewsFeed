package com.spartaboys.newsfeed.domain.follow.service.external;

import com.spartaboys.newsfeed.domain.follow.entity.Follow;
import com.spartaboys.newsfeed.domain.follow.exception.AlreadyFollowingException;
import com.spartaboys.newsfeed.domain.follow.exception.FollowErrorCode;
import com.spartaboys.newsfeed.domain.follow.exception.SelfFollowNotAllowedException;
import com.spartaboys.newsfeed.domain.follow.exception.SelfUnFollowNotAllowedException;
import com.spartaboys.newsfeed.domain.follow.repository.FollowRepository;
import com.spartaboys.newsfeed.domain.users.entity.User;
import com.spartaboys.newsfeed.domain.users.service.UserInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowCommandService {

    private final UserInternalService userInternalService;

    private final FollowRepository followRepository;

    /**
     * 로그인 유저 팔로우 메서드
     * <p>
     * 로그인한 사용자 ID(followerId), 팔로우 당하는 사람 ID(followeeId)를 기반으로
     * 로그인한 사용자가 누른 상대방을 팔로우 한다.
     * </p>
     * <h4>비즈니스 규칙</h4>
     * <ul>
     *   <li>자기 자신은 팔로우할 수 없다.</li>
     *   <li>이미 팔로우한 사용자를 다시 팔로우할 수 없다.</li>
     * </ul>
     *
     * <h4>주의 사항</h4>
     * <ul>
     *   <li>existsByFollowerAndFollowee → save 사이에 <b>동시성 이슈</b>가 발생할 수 있다.
     *       즉, 두 요청이 동시에 들어올 경우 중복 팔로우 레코드가 삽입될 수 있음.</li>
     * </ul>
     *
     * @param followerId "팔로우 하는 사람" (팔로워, 주체)
     * @param followeeId "팔로우 당하는 사람" (피팔로워, 대상)
     * @throws SelfFollowNotAllowedException 자기 자신을 팔로우하는 경우
     * @throws AlreadyFollowingException     이미 팔로우한 사용자를 중복 팔로우하는 경우
     */
    public void followUser(Long followerId, Long followeeId) {

        if (followerId.equals(followeeId)) {
            throw new SelfFollowNotAllowedException(FollowErrorCode.SELF_FOLLOW_NOT_ALLOWED);
        }

        User follower = userInternalService.getUserObjectById(followerId);
        User followee = userInternalService.getUserObjectById(followeeId);

        // TODO : 팔로우 동시성 문제 : 비관적 Lock
        if (followRepository.existsByFollowerAndFollowee(follower, followee)) {
            throw new AlreadyFollowingException(FollowErrorCode.ALREADY_FOLLOWING);
        }

        followRepository.save(
                Follow.create(follower, followee)
        );
    }

    /**
     * 로그인 유저 언팔로우 메서드
     * <p>로그인한 사용자 ID(followerId), 언팔로우 대상 사용자 ID(followeeId)를 기반으로
     * 팔로우 관계를 해제한다.</p>
     *
     * <h3>제약사항</h3>
     * <ul>
     *   <li>자기 자신은 언팔로우할 수 없으며, 시도 시 {@link SelfUnFollowNotAllowedException} 발생</li>
     *   <li>팔로우 관계가 존재하지 않는 경우에도 조용히(delete 쿼리 no-op) 처리됨 → 멱등성(idempotent) 보장</li>
     * </ul>
     *
     * @param followerId "언팔로우 하는 사람" (팔로워, 주체)
     * @param followeeId "언팔로우 당하는 사람" (피팔로워, 대상)
     * @throws SelfUnFollowNotAllowedException 자기 자신을 언팔로우하려는 경우
     */
    public void unFollowUser(Long followerId, Long followeeId) {

        if (followerId.equals(followeeId)) {
            throw new SelfUnFollowNotAllowedException(FollowErrorCode.SELF_UNFOLLOW_NOT_ALLOWED);
        }

        followRepository.deleteByFollowerIdAndFolloweeId(followerId, followeeId);
    }
}
