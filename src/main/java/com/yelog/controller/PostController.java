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
    public Map<String, String> post(@RequestBody @Valid PostCreate params) throws Exception {

        log.info("params = {}", params.toString());

        return Map.of();
    }
}
