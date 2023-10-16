package com.yelog.service;

import com.yelog.crypto.PasswordEncoder;
import com.yelog.domain.User;
import com.yelog.exception.AlreadyExistsEmailException;
import com.yelog.exception.InvalidSigninInformation;
import com.yelog.repository.UserRepository;
import com.yelog.request.Login;
import com.yelog.request.SignUp;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void test1() {
        //given
        SignUp signUp = SignUp.builder()
                .email("dldpcks34@naver.com")
                .password("1234")
                .name("이예찬")
                .build();

        //when
        authService.signUp(signUp);

        //then
        assertThat(userRepository.count()).isEqualTo(1);
        User user = userRepository.findAll().iterator().next();
        assertThat(user.getPassword()).isNotEqualTo("1234");
        assertThat(user.getPassword()).isNotNull();
        assertThat(user.getEmail()).isEqualTo("dldpcks34@naver.com");
    }

    @Test
    @DisplayName("회원가입시 중복된 이메일")
    void test2() {
        //given
        User user = User.builder()
                .email("dldpcks34@naver.com")
                .password("1234")
                .name("김예찬")
                .build();
        userRepository.save(user);

        SignUp signUp = SignUp.builder()
                .email("dldpcks34@naver.com")
                .password("1234")
                .name("이예찬")
                .build();

        //when & then
        assertThrows(AlreadyExistsEmailException.class, () ->
                authService.signUp(signUp));
    }

    @Test
    @DisplayName("로그인 성공")
    void test3() {
        PasswordEncoder encoder = new PasswordEncoder();
        String encrpytedPassword = encoder.encrpyt("1234");
        //given
        User user = User.builder()
                .email("dldpcks34@naver.com")
                .password(encrpytedPassword)
                .name("김예찬")
                .build();
        userRepository.save(user);

        Login login = Login.builder()
                .email("dldpcks34@naver.com")
                .password("1234")
                .build();

        Long userId = authService.signin(login);

        //when & then
        assertThat(userId).isNotNull();
    }

    @Test
    @DisplayName("로그인 비밀번호 틀림")
    void test4() {
        //given
        PasswordEncoder encoder = new PasswordEncoder();
        String encrpytedPassword = encoder.encrpyt("1234");
        //given
        User user = User.builder()
                .email("dldpcks34@naver.com")
                .password(encrpytedPassword)
                .name("김예찬")
                .build();
        userRepository.save(user);

        Login login = Login.builder()
                .email("dldpcks34@naver.com")
                .password("5678")
                .build();

        assertThrows(InvalidSigninInformation.class, ()->authService.signin(login));
    }

}