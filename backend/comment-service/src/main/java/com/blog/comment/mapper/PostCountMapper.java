package com.blog.comment.mapper;

import org.apache.ibatis.annotations.Param;

public interface PostCountMapper {
    int updateCommentCount(@Param("id") Long id, @Param("count") int count);
}
