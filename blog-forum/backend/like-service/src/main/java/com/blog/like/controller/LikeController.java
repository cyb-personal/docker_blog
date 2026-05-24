package com.blog.like.controller;

import com.blog.common.Result;
import com.blog.like.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/do")
    public Result<?> like(@RequestBody Map<String, Long> params) {
        Long postId = params.get("postId");
        Long userId = params.get("userId");
        boolean success = likeService.like(postId, userId);
        if (!success) {
            return Result.error("已经点赞过了");
        }
        int count = likeService.count(postId);
        Map<String, Object> data = new HashMap<>();
        data.put("liked", true);
        data.put("count", count);
        return Result.success(data);
    }

    @PostMapping("/undo")
    public Result<?> unlike(@RequestBody Map<String, Long> params) {
        Long postId = params.get("postId");
        Long userId = params.get("userId");
        likeService.unlike(postId, userId);
        int count = likeService.count(postId);
        Map<String, Object> data = new HashMap<>();
        data.put("liked", false);
        data.put("count", count);
        return Result.success(data);
    }

    @GetMapping("/count/{postId}")
    public Result<?> count(@PathVariable Long postId) {
        int count = likeService.count(postId);
        return Result.success(count);
    }

    @GetMapping("/status")
    public Result<?> status(@RequestParam Long postId, @RequestParam Long userId) {
        boolean liked = likeService.isLiked(postId, userId);
        Map<String, Object> data = new HashMap<>();
        data.put("liked", liked);
        data.put("count", likeService.count(postId));
        return Result.success(data);
    }
}
