package com.spartaboys.newsfeed.domain.auth.controller;

import com.spartaboys.newsfeed.domain.auth.JWTConstant;
import com.spartaboys.newsfeed.domain.auth.dto.*;
import com.spartaboys.newsfeed.domain.auth.JwtTokenUtil;
import com.spartaboys.newsfeed.domain.auth.service.AccessService;
import com.spartaboys.newsfeed.domain.users.entity.User;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AccessController {

    private final AccessService accessService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,
                                   HttpServletRequest request) throws AuthException {

        LoginResponse response = accessService.login(loginRequest);

        //로그인 아이디를 받아서 회원 정보 조회 -> 없을시 예외처리
        User user = accessService.findByEmail(loginRequest.getEmail());

        // 로그인 아이디나 비밀번호가 틀린 경우 -> 예외처리
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message","로그인 아이디 또는 비밀번호가 틀렸습니다"));
        }

        // 서명 과 완료를 JWTToken에 부여
        String accessToken = JwtTokenUtil.createToken(user.getEmail(),
                JWTConstant.JWT_SECRET ,
                JWTConstant.ACCESS_EXP_MS);
        long expireTimesMs = 1000 * 60 * 60; // 토큰 유효 시간

        // String token = JwtTokenUtil.createToken(user.getLoginId(), accessToken , expireTimesMs);

       //세션에 저장
//        HttpSession session = request.getSession(true);
//        session.setAttribute("LOGIN_USER_ID", user.getId());
//        session.setAttribute("LOGIN_ID", user.getEmail());

        String token = (String) request.getAttribute("JWT_TOKEN");

        System.out.println(token);

        // Map 그대로 반환 Json
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION,"Bearer " + accessToken)
                .body(Map.of("tokenType","Bearer","accessToken", accessToken));
//        Map.of(
//               "tokenType","Bearer",
//               "accessToken", response.getToken(),
//                "expiresIn", expireTimesMs / 1000 * 60 * 60
//        )
    }

    @PostMapping("/sign-up")
    public UserSignUpResponse signUp(@RequestBody UserSignUpRequest signUpRequest){
        return accessService.SignUp(signUpRequest);
    }

    @DeleteMapping("/withdrawal")
    public ResponseEntity<Void> deleteUser(HttpServletRequest request,
                                           @RequestBody DeleteRequestUser deleteRequestUser){

        HttpSession session = request.getSession(false);
        if (session == null) return ResponseEntity.status(401).build();

        Long id = (Long) session.getAttribute("LOGIN_USER_ID");
        if (id == null) return ResponseEntity.status(401).build();

        accessService.deleteUserByPassword(deleteRequestUser);

        session.invalidate();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logOut(@RequestBody HttpServletRequest request){
        accessService.logOut(request);

        return ResponseEntity.noContent().build();
    }

}
