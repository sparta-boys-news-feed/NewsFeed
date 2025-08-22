package com.spartaboys.newsfeed.domain.auth.controller;

import com.spartaboys.newsfeed.domain.auth.dto.*;
import com.spartaboys.newsfeed.domain.auth.JwtTokenUtil;
import com.spartaboys.newsfeed.domain.auth.service.AccessService;
import com.spartaboys.newsfeed.domain.users.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AccessController {

    private final AccessService accessService;

    //    @Value("${jwt.secretKey}") //
//    String JWT_SECRET;
    private static final String JWT_SECRET = "super-secret-key-change-me-32bytes-minimum-aaaaaaaaaaaa";
    private static final long ACCESS_EXP_MS = 15 * 60 * 1000L;

    @PostMapping("/sign-in")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,
                                   HttpServletRequest request) {

        LoginResponse response = accessService.login(loginRequest);

        //로그인 아이디를 받아서 회원 정보 조회 -> 없을시 예외처리
        User user = accessService.findByLoginId(loginRequest.getEmail());

        // 로그인 아이디나 비밀번호가 틀린 경우 -> 예외처리
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("message","로그인 아이디 또는 비밀번호가 틀렸습니다"));
        }

        //비밀번호 검증

        //JWT 토큰 생성

        // 서명 과 완료를 JWTToken에 부여
        String accessToken = JwtTokenUtil.createToken(user.getEmail(), JWT_SECRET, ACCESS_EXP_MS);
        long expireTimesMs = 1000 * 60 * 60; // 토큰 유효 시간

        // String token = JwtTokenUtil.createToken(user.getLoginId(), accessToken , expireTimesMs);

        //세션에 저장
        HttpSession session = request.getSession(true);
        session.setAttribute("LOGIN_USER_ID", user.getId());
        session.setAttribute("LOGIN_ID", user.getEmail());

        // Map 그대로 반환 Json
        return ResponseEntity.ok(Map.of(
                "tokenType","Bearer",
                "accessToken", response.getToken(),
                "expiresIn", expireTimesMs / 1000 * 60 * 60
        ));
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
