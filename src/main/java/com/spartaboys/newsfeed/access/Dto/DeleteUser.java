package com.spartaboys.newsfeed.access.Dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteUser {

    private String loginId;
    private String password;

}
