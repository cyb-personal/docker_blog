-- 创建数据库
CREATE DATABASE IF NOT EXISTS blog_forum DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE blog_forum;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id`          BIGINT(20)   NOT NULL AUTO_INCREMENT  COMMENT '用户ID',
    `username`    VARCHAR(50)  NOT NULL                 COMMENT '用户名',
    `password`    VARCHAR(128) NOT NULL                 COMMENT '密码(BCrypt加密)',
    `nickname`    VARCHAR(50)  DEFAULT NULL             COMMENT '昵称',
    `avatar`      VARCHAR(255) DEFAULT NULL             COMMENT '头像URL',
    `email`       VARCHAR(100) DEFAULT NULL             COMMENT '邮箱',
    `bio`         VARCHAR(255) DEFAULT NULL             COMMENT '个人简介',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 帖子表
CREATE TABLE IF NOT EXISTS `post` (
    `id`          BIGINT(20)   NOT NULL AUTO_INCREMENT  COMMENT '帖子ID',
    `user_id`     BIGINT(20)   NOT NULL                 COMMENT '作者ID',
    `title`       VARCHAR(200) NOT NULL                 COMMENT '标题',
    `content`     TEXT         NOT NULL                 COMMENT '内容',
    `category`    VARCHAR(50)  DEFAULT 'general'        COMMENT '分类',
    `like_count`  INT(11)      NOT NULL DEFAULT 0       COMMENT '点赞数',
    `comment_count` INT(11)    NOT NULL DEFAULT 0       COMMENT '评论数',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子表';

-- 评论表
CREATE TABLE IF NOT EXISTS `comment` (
    `id`          BIGINT(20)   NOT NULL AUTO_INCREMENT  COMMENT '评论ID',
    `post_id`     BIGINT(20)   NOT NULL                 COMMENT '所属帖子ID',
    `user_id`     BIGINT(20)   NOT NULL                 COMMENT '评论者ID',
    `content`     TEXT         NOT NULL                 COMMENT '评论内容',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_post_id` (`post_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 点赞表(MySQL备份，主要计数存储在Redis)
CREATE TABLE IF NOT EXISTS `likes` (
    `id`          BIGINT(20) NOT NULL AUTO_INCREMENT  COMMENT '主键',
    `post_id`     BIGINT(20) NOT NULL                 COMMENT '帖子ID',
    `user_id`     BIGINT(20) NOT NULL                 COMMENT '用户ID',
    `create_time` DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_post_user` (`post_id`, `user_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点赞记录表';

-- 插入测试用户（密码明文 123456）
INSERT INTO `user` (`username`, `password`, `nickname`, `email`, `bio`) VALUES
('admin', '123456', '管理员',  'admin@blog.com', '系统管理员'),
('alice', '123456', '爱丽丝',  'alice@blog.com', '热爱生活的程序员'),
('bob',   '123456', '鲍勃',    'bob@blog.com',   '摄影爱好者');

-- 插入测试帖子
INSERT INTO `post` (`user_id`, `title`, `content`, `category`, `like_count`, `comment_count`) VALUES
(2, 'Spring Cloud 微服务架构实践', '本文分享了在生产环境中搭建 Spring Cloud 微服务架构的经验与踩坑记录...', '技术', 12, 3),
(3, '旅行摄影入门技巧', '想要拍出好的旅行照片？这10个技巧你一定要知道...', '摄影', 8, 2),
(2, 'Java 8 Lambda 表达式详解', 'Lambda 表达式是 Java 8 引入的重要特性，本文详细讲解其用法...', '技术', 15, 5);

-- 插入测试评论
INSERT INTO `comment` (`post_id`, `user_id`, `content`) VALUES
(1, 3, '写得很棒，收藏了！'),
(1, 2, '感谢分享，对我很有帮助'),
(1, 1, '期待更多微服务相关的文章'),
(2, 2, '这些技巧很实用，下次旅行试试'),
(2, 1, '摄影小白学到了很多'),
(3, 3, '讲得很清晰，豁然开朗'),
(3, 2, '配合实战案例就更好了'),
(3, 1, '总结得很到位');
