package com.yelog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yelog.domain.Session;
import com.yelog.domain.User;
import com.yelog.repository.SessionRepository;
import com.yelog.repository.UserRepository;
import com.yelog.request.Login;
import com.yelog.request.SignUp;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공")
    void test() throws Exception {
        //given
        userRepository.save(User.builder()
                .name("이예찬")
                .email("dldpcks34@naver.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("dldpcks34@naver.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        //then
        mockMvc.perform(post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    @Transactional
    @DisplayName("로그인 성공 후 세션 1개 생성")
    void test2() throws Exception {
        //given
        User user = userRepository.save(User.builder()
                .name("이예찬")
                .email("dldpcks34@naver.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("dldpcks34@naver.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        //then
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        assertThat(user.getSessions().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("로그인 성공 후 세션 응답")
    void test3() throws Exception {
        //given
        User user = userRepository.save(User.builder()
                .name("이예찬")
                .email("dldpcks34@naver.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("dldpcks34@naver.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        //then
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", Matchers.notNullValue()))
                .andDo(print());

    }

    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지 접속")
    void test4() throws Exception {
        //given
        User user = User.builder()
                .name("이예찬")
                .email("dldpcks34@naver.com")
                .password("1234")
                .build();
        Session session = user.addSession();
        userRepository.save(user);

        Login login = Login.builder()
                .email("dldpcks34@naver.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        //then
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 후 검증되지 않은 세견값으로 권한이 필요한 페이지에 접속할 수 없다.")
    void test5() throws Exception {
        //given
        User user = User.builder()
                .name("이예찬")
                .email("dldpcks34@naver.com")
                .password("1234")
                .build();
        Session session = user.addSession();
        userRepository.save(user);

        Login login = Login.builder()
                .email("dldpcks34@naver.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        //then
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken() + "-")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입")
    void test6() throws Exception {
        //given
        SignUp signUp = SignUp.builder()
                .email("dldpcks34@naver.com")
                .password("1234")
                .name("이예찬")
                .build();

        //then
        mockMvc.perform(post("/auth/signup")
                        .content(objectMapper.writeValueAsString(signUp))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

}