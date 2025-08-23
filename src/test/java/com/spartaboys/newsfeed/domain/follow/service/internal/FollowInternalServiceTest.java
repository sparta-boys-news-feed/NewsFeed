package com.spartaboys.newsfeed.domain.follow.service.internal;

import com.spartaboys.newsfeed.domain.follow.entity.Follow;
import com.spartaboys.newsfeed.domain.follow.repository.FollowRepository;
import com.spartaboys.newsfeed.domain.users.entity.User;
import com.spartaboys.newsfeed.domain.users.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class FollowInternalServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FollowRepository followRepository;

    @Autowired
    FollowInternalService followInternalService;

    @Test
    @DisplayName("내가 친구로 추가한 사람 목록 조회")
    void getFolloweesByUserId() {
        // given
        User loginUser = createTestUser("test1@test.com", "test1");
        User test2 = createTestUser("test2@test.com", "test2");
        User test3 = createTestUser("test3@test.com", "test3");
        User test4 = createTestUser("test4@test.com", "test4");
        User test5 = createTestUser("test5@test.com", "test5");
        User test6 = createTestUser("test6@test.com", "test6");
        userRepository.saveAll(List.of(loginUser, test2, test3, test4, test5, test6));

        followRepository.saveAll(
                List.of(
                        Follow.create(loginUser, test2),
                        Follow.create(loginUser, test3),
                        Follow.create(loginUser, test4),
                        Follow.create(loginUser, test5),
                        Follow.create(loginUser, test6)
                )
        );

        // when
        List<Follow> followees = followInternalService.getFolloweesByUserId(loginUser.getId());

        // then
        assertThat(followees)
                .extracting("followee.nickname")
                .contains(
                        "test2", "test3", "test4", "test5", "test6"
                );
    }

    private User createTestUser(String email, String nickname) {
        return User.builder()
                .email(email)
                .nickname(nickname)
                .password("123456789123456789123456789123456789123456789123456789123456789123456789")
                .build();
    }
}