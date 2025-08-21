package com.spartaboys.newsfeed.access.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequest {

    String username;
    String loginId;
    String email;
    String password;

}
