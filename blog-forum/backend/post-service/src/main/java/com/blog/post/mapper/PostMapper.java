package com.blog.post.mapper;

import com.blog.post.entity.Post;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PostMapper {
    Post findById(@Param("id") Long id);
    List<Post> findAll(@Param("offset") int offset, @Param("limit") int limit);
    List<Post> findByUserId(@Param("userId") Long userId);
    int insert(Post post);
    int update(Post post);
    int deleteById(@Param("id") Long id);
    int updateLikeCount(@Param("id") Long id, @Param("count") int count);
    int updateCommentCount(@Param("id") Long id, @Param("count") int count);
}
