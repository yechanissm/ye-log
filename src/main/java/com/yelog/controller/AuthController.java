package com.yelog.controller;

import com.yelog.request.Login;
import com.yelog.response.SessionResponse;
import com.yelog.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.Base64;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final static String KEY = "4+rwsQ2gJvu0yrkdJnwftn9o30Das9vy4XpI9+t2G3M=";

    private final AuthService authService;

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login) {
        // DB 접근 & 토큰 발근
        Long userId = authService.signin(login);

        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(KEY));

        String jws = Jwts.builder().setSubject(String.valueOf(userId)).signWith(key).compact();

        return new SessionResponse(jws);
    }


}
