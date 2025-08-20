package com.spartaboys.newsfeed.domain.users.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String newPasswordConfirm;
}
