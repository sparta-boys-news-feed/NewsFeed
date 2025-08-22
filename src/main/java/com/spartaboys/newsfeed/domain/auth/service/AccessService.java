package com.spartaboys.newsfeed.domain.auth.service;

import com.spartaboys.newsfeed.domain.auth.dto.*;
import com.spartaboys.newsfeed.domain.auth.JwtTokenUtil;
import com.spartaboys.newsfeed.domain.auth.PasswordEncoder;
import com.spartaboys.newsfeed.domain.auth.repository.AccessRepository;
import com.spartaboys.newsfeed.domain.users.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccessService {

    private final AccessRepository accessRepository;
    private final PasswordEncoder passwordEncoder;

    //loginId로 엔티티를 호출 없을시에는 예외 처리
    @Transactional(readOnly = true)
    public User findByLoginId(String email) {

        return accessRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }

    // LoginId를 통해서 유저찾기
    @Transactional(readOnly = true)
    public User getLoginUserByLoginId(String email) {

        return accessRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 로그인 아이디입니다."));
    }

    @Transactional
    public UserSignUpResponse SignUp(UserSignUpRequest request){

        if (accessRepository.existsByEmail(request.getEmail())){
            throw new DuplicateKeyException("이미 사용 중인 로그인 아이디입니다.");
        }


        String hashed = passwordEncoder.encode(8,request.getPassword());

        User user = User.builder()
                .email(request.getEmail())
                .password(hashed)
                .build();

        return new UserSignUpResponse(user.getEmail(), request.getUsername(), user.getEmail());
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest loginRequest) {

        User user = getLoginUserByLoginId(loginRequest.getEmail());

        String token = JwtTokenUtil.createToken(user.getEmail(),
                "super-secret-key-change-me-32bytes-minimum-aaaaaaaaaaaa",
                15 * 60 * 1000L);


        return new LoginResponse(user.getEmail(), token);
    }

    @Transactional
    public void deleteUserByPassword(DeleteRequestUser deleteRequestUser) {
        Optional<User> user = Optional.ofNullable(accessRepository.findByEmail(deleteRequestUser.getEmail())
                .orElseThrow(() -> new IllegalArgumentException()));

        System.out.println("회원탈퇴를 위해서 비밀번호를 다시 입력해주세요.");
        if (!passwordEncoder.matches(deleteRequestUser.getPassword(), user.get().getPassword())) {
            throw new IllegalArgumentException("비밀번호가 맞지 않습니다.");
        }


        accessRepository.deleteById(user.get().getId());
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
