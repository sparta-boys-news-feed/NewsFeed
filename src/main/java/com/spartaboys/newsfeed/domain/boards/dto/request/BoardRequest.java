package com.spartaboys.newsfeed.domain.boards.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardRequest {
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(min = 2, max = 50, message = "제목은 2자 이상 50자 이하로 입력해주세요.")
    String title;
    @NotBlank(message = "내용을 입력해주세요.")
    @Size(min = 2, max = 1000, message = "제목은 2자 이상 1000자 이하로 입력해주세요.")
    String content;
}
