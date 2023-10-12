package com.yelog.controller;

import com.yelog.request.PostCreate;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
public class PostController {

    //Http Method
    // GET, POST, PUT, PATCH, DELETE, OPTIONS< HEAD, TRACE, CONNECT

    @PostMapping("/posts")
    public String post(@RequestBody PostCreate params) throws Exception {
        //데이터를 검증하는 이유
        //1. client 개발자가 실수로 값을 안보낼 수 있다.
        //2. client 버그로 값이 누락될 수 있다.
        //3. 외부인이 값을 임으로 조작할 수 있다.
        //4. DB에 값을 저장할 때 의도치 않은 오류가 발생할 수 있다.
        //5. 서버 개발자의 편안함을 위해서
        log.info("params = {}", params.toString());
        String title = params.getTitle();
        if(title==null || title.equals("")){
            //이러한 검증은 빡세다(노가다)
            //개발팁 -> 무언가 3번이상 반복작업을 할 때 내가 뭔가 잘못하고 있는건 아닌지 의심한다.
            //누락 가능성
            //생각보다 검증해야 할게 많다.
            //뭔가 개발자 스럽지 않다.
            throw new Exception("타이틀 값이 없어요");
        }
        String content = params.getContent();
        if(content==null || content.equals("")){
            //error

        }
        return "Hello World";
    }
}
