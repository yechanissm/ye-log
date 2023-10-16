package com.yelog.controller;

import com.yelog.config.AppConfig;
import com.yelog.request.SignUp;
import com.yelog.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AppConfig appConfig;
    private final AuthService authService;

    @GetMapping("/auth/login")
    public String login() {
        return "로그인 페이지입니다.";
    }


    @PostMapping("/auth/signup")
    public void signUp(@RequestBody SignUp signUp) {
        authService.signUp(signUp);
    }


}
