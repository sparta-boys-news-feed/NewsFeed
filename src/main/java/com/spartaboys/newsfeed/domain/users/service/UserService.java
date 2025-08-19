package com.spartaboys.newsfeed.domain.users.service;

import com.spartaboys.newsfeed.domain.users.dto.response.UserPublicResponse;
import com.spartaboys.newsfeed.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private UserRepository userRepository;

    private UserPublicResponse getUserById

}
