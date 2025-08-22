package com.spartaboys.newsfeed.domain.users.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest (
    @NotBlank
    @Size(min = 2, max = 100)
    String nickname
){
}
