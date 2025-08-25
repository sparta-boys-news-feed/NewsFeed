package com.spartaboys.newsfeed.domain.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignUpResponse {

    String userName;
    String email;

    public UserSignUpResponse(String email, String userName) {
        this.email = email;
        this.userName = userName;
    }
}
