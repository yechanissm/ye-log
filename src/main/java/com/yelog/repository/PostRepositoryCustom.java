package com.yelog.repository;

import com.yelog.domain.Post;
import com.yelog.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
