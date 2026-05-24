package com.blog.like.service;

import com.blog.like.entity.Likes;
import com.blog.like.mapper.LikeMapper;
import com.blog.like.mapper.PostCountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LikeService {

    private static final String LIKE_COUNT_KEY = "like:count:";
    private static final String LIKE_STATUS_KEY = "like:status:";

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private PostCountMapper postCountMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public boolean like(Long postId, Long userId) {
        Likes existing = likeMapper.findByPostIdAndUserId(postId, userId);
        if (existing != null) {
            return false;
        }
        Likes likes = new Likes();
        likes.setPostId(postId);
        likes.setUserId(userId);
        likes.setCreateTime(LocalDateTime.now());
        likeMapper.insert(likes);
        redisTemplate.opsForValue().increment(LIKE_COUNT_KEY + postId, 1);
        redisTemplate.opsForValue().set(LIKE_STATUS_KEY + postId + ":" + userId, "1");
        syncLikeCount(postId);
        return true;
    }

    public boolean unlike(Long postId, Long userId) {
        Likes existing = likeMapper.findByPostIdAndUserId(postId, userId);
        if (existing == null) {
            return false;
        }
        likeMapper.deleteByPostIdAndUserId(postId, userId);
        Long count = redisTemplate.opsForValue().decrement(LIKE_COUNT_KEY + postId);
        if (count != null && count < 0) {
            redisTemplate.opsForValue().set(LIKE_COUNT_KEY + postId, "0");
        }
        redisTemplate.opsForValue().set(LIKE_STATUS_KEY + postId + ":" + userId, "0");
        syncLikeCount(postId);
        return true;
    }

    private void syncLikeCount(Long postId) {
        int count = likeMapper.countByPostId(postId);
        postCountMapper.updateLikeCount(postId, count);
    }

    public int count(Long postId) {
        Object cached = redisTemplate.opsForValue().get(LIKE_COUNT_KEY + postId);
        if (cached != null) {
            return Integer.parseInt(cached.toString());
        }
        int count = likeMapper.countByPostId(postId);
        redisTemplate.opsForValue().set(LIKE_COUNT_KEY + postId, String.valueOf(count));
        return count;
    }

    public boolean isLiked(Long postId, Long userId) {
        Object cached = redisTemplate.opsForValue().get(LIKE_STATUS_KEY + postId + ":" + userId);
        if (cached != null) {
            return "1".equals(cached.toString());
        }
        return likeMapper.isLiked(postId, userId) > 0;
    }
}
