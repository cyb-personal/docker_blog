package com.blog.post.controller;

import com.blog.common.Result;
import com.blog.post.entity.Post;
import com.blog.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/list")
    public Result<?> list(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size) {
        List<Post> list = postService.findAll(page, size);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Long id) {
        Post post = postService.findById(id);
        if (post == null) {
            return Result.error("帖子不存在");
        }
        return Result.success(post);
    }

    @GetMapping("/user/{userId}")
    public Result<?> userPosts(@PathVariable Long userId) {
        return Result.success(postService.findByUserId(userId));
    }

    @PostMapping("/create")
    public Result<?> create(@RequestBody Post post) {
        if (post.getTitle() == null || post.getTitle().trim().isEmpty()) {
            return Result.error("标题不能为空");
        }
        Post created = postService.create(post);
        return Result.success(created);
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody Post post) {
        Post updated = postService.update(post);
        return Result.success(updated);
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        postService.delete(id);
        return Result.success();
    }
}
