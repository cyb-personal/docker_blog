package com.blog.comment.controller;

import com.blog.common.Result;
import com.blog.comment.entity.Comment;
import com.blog.comment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/post/{postId}")
    public Result<?> list(@PathVariable Long postId) {
        List<Comment> comments = commentService.findByPostId(postId);
        return Result.success(comments);
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody Comment comment) {
        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            return Result.error("评论内容不能为空");
        }
        Comment created = commentService.add(comment);
        return Result.success(created);
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        commentService.delete(id);
        return Result.success();
    }

    @GetMapping("/count/{postId}")
    public Result<?> count(@PathVariable Long postId) {
        int count = commentService.countByPostId(postId);
        return Result.success(count);
    }
}
