package com.yelog.service;

import com.yelog.domain.Post;
import com.yelog.repository.PostRepository;
import com.yelog.request.PostCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {
        //postCreate -> Entity
        Post post = new Post(postCreate.getTitle(), postCreate.getContent());
        postRepository.save(post);
    }

}
