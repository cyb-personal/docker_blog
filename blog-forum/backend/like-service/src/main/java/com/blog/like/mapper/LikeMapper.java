package com.blog.like.mapper;

import com.blog.like.entity.Likes;
import org.apache.ibatis.annotations.Param;

public interface LikeMapper {
    Likes findByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);
    int insert(Likes likes);
    int deleteByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);
    int countByPostId(@Param("postId") Long postId);
    int isLiked(@Param("postId") Long postId, @Param("userId") Long userId);
}
