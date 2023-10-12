package com.yelog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yelog.domain.Post;
import com.yelog.repository.PostRepository;
import com.yelog.request.PostCreate;
import com.yelog.request.PostEdit;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    void test() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
        //프린트 메소드는 HTTP 요청에 대한 요약을 남겨준다
    }


    @Test
    @DisplayName("/post 요청시 TITLE 값은 필수다")
    void test2() throws Exception {
        PostCreate request = PostCreate.builder()
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());
        //프린트 메소드는 HTTP 요청에 대한 요약을 남겨준다
    }

    @Test
    @DisplayName("/post 요청시 DB에 값이 저장된다")
    void test3() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
        //프린트 메소드는 HTTP 요청에 대한 요약을 남겨준다

        //then
        assertThat(postRepository.count()).isEqualTo(1);

        Post post = postRepository.findAll().get(0);
        assertThat(post.getTitle()).isEqualTo("제목입니다.");
    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        //given
        Post post = Post.builder()
                .title("01234567891234")
                .content("bar")
                .build();
        postRepository.save(post);

        //when & then
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("0123456789"))
                .andDo(print());

    }

    @Test
    @DisplayName("글 여러 조회")
    void test5() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(0,20)
                .mapToObj(i -> Post.builder()
                        .title("예차니즘 제목  " + i)
                        .content("삼환아파트  " + i)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        //when & then
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
                .andExpect(jsonPath("$[0].title").value("예차니즘 제목  19"))
                .andDo(print());

    }


    @Test
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다")
    void test6() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(0,20)
                .mapToObj(i -> Post.builder()
                        .title("예차니즘 제목  " + i)
                        .content("삼환아파트  " + i)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        //when & then
        mockMvc.perform(get("/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
                .andExpect(jsonPath("$[0].title").value("예차니즘 제목  19"))
                .andDo(print());

    }

    @Test
    @DisplayName("글 제목 수정")
    void test7() throws Exception {
        //given
        Post post = Post.builder()
                .title("예차니즘")
                .content("우성아파트")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("예차니즘")
                .content("삼환아파트")
                .build();

        //when & then
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 삭제")
    void test8() throws Exception {
        Post post = Post.builder()
                .title("예차니즘")
                .content("우성아파트")
                .build();
        postRepository.save(post);

        //expected
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }


}