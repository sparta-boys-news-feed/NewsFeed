package com.spartaboys.newsfeed.domain.auth.controller;

import com.spartaboys.newsfeed.domain.auth.dto.UserPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeController {

    @GetMapping("/me")
    public UserPrincipal me(@RequestAttribute("CURRENT_USER") UserPrincipal me) {
        return me; // me.id(), me.email() 등 사용
    }
}
