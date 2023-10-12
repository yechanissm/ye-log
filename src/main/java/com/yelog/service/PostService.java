package com.yelog.service;

import com.yelog.domain.Post;
import com.yelog.repository.PostRepository;
import com.yelog.request.PostCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate request) {
        //postCreate -> Entity
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        postRepository.save(post).getId();
    }

    public Post get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));
        return post;

    }
}
