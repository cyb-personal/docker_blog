package com.blog.post.service;

import com.blog.post.entity.Post;
import com.blog.post.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostMapper postMapper;

    public Post findById(Long id) {
        return postMapper.findById(id);
    }

    public List<Post> findAll(int page, int size) {
        int offset = (page - 1) * size;
        return postMapper.findAll(offset, size);
    }

    public List<Post> findByUserId(Long userId) {
        return postMapper.findByUserId(userId);
    }

    public Post create(Post post) {
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setCreateTime(LocalDateTime.now());
        post.setUpdateTime(LocalDateTime.now());
        postMapper.insert(post);
        return post;
    }

    public Post update(Post post) {
        post.setUpdateTime(LocalDateTime.now());
        postMapper.update(post);
        return postMapper.findById(post.getId());
    }

    public boolean delete(Long id) {
        return postMapper.deleteById(id) > 0;
    }
}
