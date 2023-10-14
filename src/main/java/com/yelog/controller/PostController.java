package com.yelog.controller;

import com.yelog.domain.Post;
import com.yelog.exception.InvalidRequest;
import com.yelog.request.PostCreate;
import com.yelog.request.PostEdit;
import com.yelog.request.PostSearch;
import com.yelog.response.PostResponse;
import com.yelog.service.PostService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request, @RequestHeader String authorization) throws Exception {
        //Case1. 저장한 데이터 Entity -> Response로 응답하기
        //Case2. 저장한 데이터의 Primary_id -> response로 응답하기
        //          Client에서는 수신 id를 post 조회 API를 통해 데이터를 수신받음
        //Case3. 응답 필요없음 -> 클라이언트에서 모든 POST 데이터를 관리함
        // Bad Case : 서버에서 -> 반드시 이렇게 합니다.! Fix XXXXX
        //          -> 서버에서 차라리 유연하게 대응하는게 좋다.
        //현재 코드는 Case3

        // 인증 방법
        // 1. Get Parameter
        // 2. Header
        if(authorization.equals("yechan")) {
            request.validate();
            postService.write(request);
        }
    }

    /**
     * /posts -> 글 전체조회(검색 + 페이징)
     * /posts/{postId} -> 글 한개만 조회
     */
    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        //응답 클래스 분리 (서비스 정책에 맞는)
        // Request 클래스 -> 요청에 대한 정책을 담아둔 클래스
        // Response 클래스 -> 서비스 정책에 맞는 로직이 들어가는 클래스
        return postService.get(postId);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request) {
        postService.edit(postId, request);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }
}
