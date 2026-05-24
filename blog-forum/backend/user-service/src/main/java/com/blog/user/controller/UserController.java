package com.blog.user.controller;

import com.blog.common.Result;
import com.blog.user.entity.User;
import com.blog.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result<?> register(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        String nickname = params.get("nickname");
        if (userService.findByUsername(username) != null) {
            return Result.error("用户名已存在");
        }
        User user = userService.register(username, password, nickname);
        user.setPassword(null);
        return Result.success(user);
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        User user = userService.findByUsername(username);
        if (user == null) {
            return Result.error("用户不存在");
        }
        if (!password.equals(user.getPassword())) {
            return Result.error("密码错误");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("token", "mock-token-" + user.getId());
        data.put("userId", user.getId());
        data.put("nickname", user.getNickname());
        return Result.success(data);
    }

    @GetMapping("/info/{id}")
    public Result<?> getUserInfo(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        user.setPassword(null);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result<?> updateProfile(@RequestBody User user) {
        User updated = userService.updateProfile(user);
        updated.setPassword(null);
        return Result.success(updated);
    }
}
