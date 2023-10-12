package com.yelog.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yelog.domain.Post;
import com.yelog.domain.QPost;
import com.yelog.request.PostSearch;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.yelog.domain.QPost.*;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(post)
                .limit(postSearch.getSize())
                .offset(postSearch.getOffset())
                .orderBy(post.id.desc())
                .fetch();
    }
}
