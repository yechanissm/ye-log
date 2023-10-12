package com.yelog.controller;

import com.yelog.request.PostCreate;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class PostController {

    //Http Method
    // GET, POST, PUT, PATCH, DELETE, OPTIONS< HEAD, TRACE, CONNECT

    @PostMapping("/posts")
    public Map<String, String> post(@RequestBody @Valid PostCreate params, BindingResult result) throws Exception {
        //데이터를 검증하는 이유
        //1. client 개발자가 실수로 값을 안보낼 수 있다.
        //2. client 버그로 값이 누락될 수 있다.
        //3. 외부인이 값을 임으로 조작할 수 있다.
        //4. DB에 값을 저장할 때 의도치 않은 오류가 발생할 수 있다.
        //5. 서버 개발자의 편안함을 위해서
        log.info("params = {}", params.toString());
        // {"title": "타이틀 값이 없습니다."} 라고 알려주고 싶을 경우
        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            FieldError firstFieldError = fieldErrors.get(0);
            String fieldName = firstFieldError.getField(); //title
            String errorMessage = firstFieldError.getDefaultMessage(); //에러 메시지

            Map<String, String> error = new HashMap<>();
            error.put(fieldName, errorMessage);
            return error;

        }
        return Map.of();
    }
}
