package com.spartaboys.newsfeed.access.service;

import com.spartaboys.newsfeed.access.Dto.*;
import com.spartaboys.newsfeed.access.JwtTokenUtil;
import com.spartaboys.newsfeed.access.repository.UserRepositorySample;
import com.spartaboys.newsfeed.domain.users.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginUserServiceSample {

    private final UserRepositorySample userRepositorySample;

    //loginId로 엔티티를 호출 없을시에는 예외 처리
    @Transactional(readOnly = true)
    public User findByLoginId(String loginId) {

        return userRepositorySample.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }

    // LoginId를 통해서 유저찾기
    @Transactional(readOnly = true)
    public User getLoginUserByLoginId(String loginId) {

        return userRepositorySample.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 로그인 아이디입니다."));
    }

    @Transactional
    public UserSignUpResponse SignUp(UserSignUpRequest request){

        if (userRepositorySample.existsByLoginId(request.getLoginId())){
            throw new DuplicateKeyException("이미 사용 중인 로그인 아이디입니다.");
        }

        User user = new User();
        user.setLoginId(request.getLoginId());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        userRepositorySample.save(user);

        return new UserSignUpResponse(user.getEmail(), request.getUsername(), user.getEmail());
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest loginRequest) {

        User user = getLoginUserByLoginId(loginRequest.getLoginId());

        String token = JwtTokenUtil.createToken(user.getEmail(),
        "super-secret-key-change-me-32bytes-minimum-aaaaaaaaaaaa",
                15 * 60 * 1000L);

        return new LoginResponse(user.getEmail(), token);
    }

    @Transactional
    public void deleteUserByid(Long id) {
        userRepositorySample.deleteById(id);
    }

    @Transactional
    public String logOut(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session != null){
            session.invalidate();
        }

        return "로그아웃이 처리되었습니다.";
    }
}
