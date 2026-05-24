package com.blog.user.mapper;

import com.blog.user.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    User findById(@Param("id") Long id);
    User findByUsername(@Param("username") String username);
    int insert(User user);
    int update(User user);
}
