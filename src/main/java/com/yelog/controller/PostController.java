package com.yelog.controller;

import com.yelog.domain.Post;
import com.yelog.request.PostCreate;
import com.yelog.service.PostService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) throws Exception {
        //Case1. 저장한 데이터 Entity -> Response로 응답하기
        //Case2. 저장한 데이터의 Primary_id -> response로 응답하기
        //          Client에서는 수신 id를 post 조회 API를 통해 데이터를 수신받음
        //Case3. 응답 필요없음 -> 클라이언트에서 모든 POST 데이터를 관리함
        // Bad Case : 서버에서 -> 반드시 이렇게 합니다.! Fix XXXXX
        //          -> 서버에서 차라리 유연하게 대응하는게 좋다.
        //현재 코드는 Case3
        postService.write(request);
    }

    /**
     * /posts -> 글 전체조회(검색 + 페이징)
     * /posts/{postId} -> 글 한개만 조회
     */
    @GetMapping("/posts/{postId}")
    public Post get(@PathVariable(name = "postId") Long id) {
        return postService.get(id);
    }
}
