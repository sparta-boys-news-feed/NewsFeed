package com.spartaboys.newsfeed.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpResponse {

    String userName;
    String email;

    public UserSignUpResponse(@Email @NotBlank String email, @NotBlank(message = "회원 이름은 필수 입력값입니다.") String username, @Email @NotBlank String email1) {

    }
}
