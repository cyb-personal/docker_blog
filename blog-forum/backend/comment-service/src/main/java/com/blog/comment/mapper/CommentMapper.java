package com.blog.comment.mapper;

import com.blog.comment.entity.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentMapper {
    Comment findById(@Param("id") Long id);
    List<Comment> findByPostId(@Param("postId") Long postId);
    int insert(Comment comment);
    int deleteById(@Param("id") Long id);
    int countByPostId(@Param("postId") Long postId);
}
