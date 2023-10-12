package com.yelog.service;

import com.yelog.domain.Post;
import com.yelog.repository.PostRepository;
import com.yelog.request.PostCreate;
import com.yelog.request.PostSearch;
import com.yelog.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.data.domain.Sort.Direction.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        //when
        postService.write(postCreate);
        Post post = postRepository.findAll().get(0);

        //then
        assertThat(postRepository.count()).isEqualTo(1);
        assertThat(post.getTitle()).isEqualTo("제목입니다.");
        assertThat(post.getContent()).isEqualTo("내용입니다.");

    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() {
        //given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        //클라이언트 요구사항
        //json 응답에서 title 길이를 최대 10글자로 해주세요.
        // -> PostResponse 클래스 확인

        //when
        PostResponse response = postService.get(requestPost.getId());

        //then
        assertNotNull(response);
        assertThat(response.getTitle()).isEqualTo("foo");
        assertThat(response.getContent()).isEqualTo("bar");
    }

    @Test
    @DisplayName("글 1페이지 조회")
    void test3() {
        //given
        List<Post> requestPosts = IntStream.range(0, 20)
                        .mapToObj(i -> Post.builder()
                                .title("예차니즘 제목  " + i)
                                .content("삼환아파트  " + i)
                                .build())
                                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // sql -> select, limit, offset
        Pageable pageable = PageRequest.of(0,5);

        PostSearch postSearch = PostSearch.builder()
                .page(1).build();


        //when
        List<PostResponse> posts = postService.getList(postSearch);

        //then
        assertThat(posts.size()).isEqualTo(10);
        assertThat(posts.get(0).getTitle()).isEqualTo("예차니즘 제목  19");
    }
}