package com.yelog.service;

import com.yelog.domain.Post;
import com.yelog.domain.PostEditor;
import com.yelog.exception.PostNotFound;
import com.yelog.repository.PostRepository;
import com.yelog.request.PostCreate;
import com.yelog.request.PostEdit;
import com.yelog.request.PostSearch;
import com.yelog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nonapi.io.github.classgraph.json.JSONUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFound());

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    // 글이 너무 많은 경우 -> 비용이 많이 든다
    // 글이 -> 100,000,000 -> 모두 조회할 경우 -> DB가 뻗는다
    // DB -> 애플리케이션 서버로 전달하는 시간, 트래픽비용이 많이 든다.
    public List<PostResponse> getList(PostSearch postSearch) {
        System.out.println("dsadsa");

        return postRepository.getList(postSearch).stream()
                .map(post -> new PostResponse(post))
                .collect(Collectors.toList());
    }

    @Transactional
    public void edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFound());

        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();

        PostEditor postEditor = editorBuilder.title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        post.edit(postEditor);
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFound());

        // -> 존재하는 경우
        postRepository.delete(post);
    }
}
