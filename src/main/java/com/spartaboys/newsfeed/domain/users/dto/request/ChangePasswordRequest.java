package com.spartaboys.newsfeed.domain.users.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChangePasswordRequest (
    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String currentPassword,

    @NotBlank
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    String newPassword,

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String newPasswordConfirm
) {
    @Override
    public String toString() {
        return "ChangePasswordRequest{***masked***}";
    }
}
