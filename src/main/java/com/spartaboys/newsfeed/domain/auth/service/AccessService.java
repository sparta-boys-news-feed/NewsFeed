package com.spartaboys.newsfeed.domain.auth.service;

import com.spartaboys.newsfeed.domain.auth.JWTConstant;
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
    public User findByEmail(String email) {

        return accessRepository.findByEmail(email);
    }

    @Transactional
    public UserSignUpResponse SignUp(UserSignUpRequest request){

        if (accessRepository.existsByEmail(request.getEmail())){
            throw new DuplicateKeyException("이미 사용 중인 로그인 아이디입니다.");
        }


        String hashed = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .email(request.getEmail())
                .nickname(request.getUsername())
                .password(hashed)
                .build();

        User saved = accessRepository.save(user);

        return new UserSignUpResponse(user.getEmail(), request.getUsername());
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest loginRequest) {

        User byEmail = accessRepository.findByEmail(loginRequest.getEmail());

        if (byEmail == null){
            throw new IllegalArgumentException("아이디가 맞지 않습니다.");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(),byEmail.getPassword())){
            throw new IllegalArgumentException("비밀번호가 맞지 않습니다.");
        }

        String token = JwtTokenUtil.createToken(byEmail.getEmail(),
                JWTConstant.JWT_SECRET,
               JWTConstant.ACCESS_EXP_MS);


        return new LoginResponse(byEmail.getEmail(), token);
    }

    @Transactional
    public void deleteUserByPassword(DeleteRequestUser deleteRequestUser) {
        Optional<User> user = Optional.ofNullable(accessRepository.findByEmail(deleteRequestUser.getEmail()));

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
