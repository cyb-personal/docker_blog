package com.blog.comment.service;

import com.blog.comment.entity.Comment;
import com.blog.comment.mapper.CommentMapper;
import com.blog.comment.mapper.PostCountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private PostCountMapper postCountMapper;

    public List<Comment> findByPostId(Long postId) {
        return commentMapper.findByPostId(postId);
    }

    public Comment add(Comment comment) {
        comment.setCreateTime(LocalDateTime.now());
        commentMapper.insert(comment);
        syncCommentCount(comment.getPostId());
        return comment;
    }

    public boolean delete(Long id) {
        Comment comment = commentMapper.findById(id);
        if (comment == null) return false;
        commentMapper.deleteById(id);
        syncCommentCount(comment.getPostId());
        return true;
    }

    public int countByPostId(Long postId) {
        return commentMapper.countByPostId(postId);
    }

    private void syncCommentCount(Long postId) {
        int count = commentMapper.countByPostId(postId);
        postCountMapper.updateCommentCount(postId, count);
    }
}
