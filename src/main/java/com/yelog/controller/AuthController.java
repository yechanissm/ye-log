package com.yelog.controller;

import com.yelog.domain.User;
import com.yelog.exception.InvalidRequest;
import com.yelog.exception.InvalidSigninInformation;
import com.yelog.repository.UserRepository;
import com.yelog.request.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @PostMapping("/auth/login")
    public User login(@RequestBody Login login)  {
        // json 아이디,비밀번호
        log.info(">>> login={}", login);

        // DB 조회
        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(() -> new InvalidSigninInformation());

        return user;
        // 토큰 발근


    }
}
