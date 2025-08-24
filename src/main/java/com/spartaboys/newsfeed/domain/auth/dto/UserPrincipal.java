package com.spartaboys.newsfeed.domain.auth.dto;

import lombok.Getter;

@Getter
public class UserPrincipal {

    private final Long id;
    private final String nickname;

    public UserPrincipal(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
