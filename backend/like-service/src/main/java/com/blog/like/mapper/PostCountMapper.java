package com.blog.like.mapper;

import org.apache.ibatis.annotations.Param;

public interface PostCountMapper {
    int updateLikeCount(@Param("id") Long id, @Param("count") int count);
}
