package com.yelog.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class PostControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Test
    void test() throws Exception {
        //글제목
        //글내용

        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"제목입니디.\", \"content\":\"내용입니다.\"}")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"))
                .andDo(MockMvcResultHandlers.print());
        //프린트 메소드는 HTTP 요청에 대한 요약을 남겨준다
    }


    @Test
    @DisplayName("/post 요청시 TITLE 값은 필수다")
    void test2() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"\", \"content\":\"내용입니다.\"}")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"))
                .andDo(MockMvcResultHandlers.print());
        //프린트 메소드는 HTTP 요청에 대한 요약을 남겨준다
    }

}