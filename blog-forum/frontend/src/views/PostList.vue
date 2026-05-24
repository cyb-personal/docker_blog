<template>
  <div class="post-list-page">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">热门帖子</h1>
        <p class="page-subtitle">发现最新有趣的内容</p>
      </div>
      <el-button type="primary" size="medium" class="create-btn" @click="$router.push('/create')" round>
        <i class="el-icon-edit"></i> 发布帖子
      </el-button>
    </div>

    <div v-loading="loading" class="post-list">
      <div v-for="post in posts" :key="post.id" class="post-card" @click="$router.push('/post/' + post.id)">
        <div class="post-main">
          <div class="post-author">
            <el-avatar :size="40" class="author-avatar">{{ post.authorName ? post.authorName.charAt(0) : '?' }}</el-avatar>
            <div class="author-info">
              <span class="author-name">{{ post.authorName || '未知用户' }}</span>
              <span class="post-time">{{ formatTime(post.createTime) }}</span>
            </div>
            <el-tag size="mini" type="success" v-if="post.category" class="category-tag">{{ post.category }}</el-tag>
          </div>
          <h3 class="post-title">{{ post.title }}</h3>
          <p class="post-excerpt">{{ truncateContent(post.content) }}</p>
          <div class="post-stats">
            <span class="stat-item">
              <i class="el-icon-star-off"></i> {{ post.likeCount || 0 }}
            </span>
            <span class="stat-item">
              <i class="el-icon-chat-line-square"></i> {{ post.commentCount || 0 }}
            </span>
          </div>
        </div>
      </div>
      <el-empty v-if="!loading && posts.length === 0" description="暂无帖子"></el-empty>
    </div>

    <div class="pagination-wrap" v-if="total > size">
      <el-pagination
        background
        layout="prev, pager, next"
        :total="total"
        :page-size="size"
        :current-page.sync="page"
        @current-change="loadPosts"
      ></el-pagination>
    </div>
  </div>
</template>

<script>
import { getPostList } from '../api/post'
import { getLikeCount } from '../api/like'
import { getCommentCount } from '../api/comment'

export default {
  name: 'PostList',
  data() {
    return {
      posts: [],
      loading: false,
      page: 1,
      size: 10,
      total: 0
    }
  },
  created() {
    this.loadPosts()
  },
  methods: {
    async loadPosts() {
      this.loading = true
      try {
        const res = await getPostList(this.page, this.size)
        this.posts = res || []
        this.total = (res && res.length) || 0
        this.fetchLiveCounts()
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    async fetchLiveCounts() {
      const posts = this.posts
      if (!posts.length) return
      const tasks = posts.map(post =>
        Promise.all([
          getLikeCount(post.id).catch(() => 0),
          getCommentCount(post.id).catch(() => 0)
        ]).then(([likeCount, commentCount]) => {
          post.likeCount = likeCount
          post.commentCount = commentCount
        })
      )
      await Promise.all(tasks)
    },
    formatTime(time) {
      if (!time) return ''
      const d = new Date(time)
      const now = new Date()
      const diff = now - d
      if (diff < 3600000) return Math.floor(diff / 60000) + '分钟前'
      if (diff < 86400000) return Math.floor(diff / 3600000) + '小时前'
      const month = (d.getMonth() + 1).toString().padStart(2, '0')
      const day = d.getDate().toString().padStart(2, '0')
      return month + '-' + day
    },
    truncateContent(content) {
      if (!content) return ''
      return content.length > 120 ? content.substring(0, 120) + '...' : content
    }
  }
}
</script>

<style scoped>
.post-list-page {
  animation: fadeIn 0.3s ease;
}
@keyframes fadeIn { from { opacity: 0; transform: translateY(12px); } to { opacity: 1; transform: translateY(0); } }
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 24px;
}
.page-title {
  font-size: 26px;
  color: #1a1a2e;
  font-weight: 700;
}
.page-subtitle {
  color: #999;
  font-size: 14px;
  margin-top: 4px;
}
.create-btn {
  padding: 10px 24px;
  font-size: 14px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border: none;
}
.post-card {
  background: #fff;
  border-radius: 12px;
  padding: 20px 24px;
  margin-bottom: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}
.post-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(102, 126, 234, 0.12);
}
.post-author {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}
.author-avatar {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: #fff;
  margin-right: 10px;
  cursor: pointer;
}
.author-info {
  display: flex;
  flex-direction: column;
  flex: 1;
}
.author-name {
  font-size: 14px;
  font-weight: 600;
  color: #333;
}
.post-time {
  font-size: 12px;
  color: #999;
  margin-top: 2px;
}
.category-tag { margin-left: 8px; }
.post-title {
  font-size: 18px;
  font-weight: 600;
  color: #1a1a2e;
  margin-bottom: 8px;
  line-height: 1.5;
}
.post-excerpt {
  font-size: 14px;
  color: #666;
  line-height: 1.7;
  margin-bottom: 14px;
}
.post-stats {
  display: flex;
  gap: 20px;
}
.stat-item {
  font-size: 13px;
  color: #999;
  display: flex;
  align-items: center;
  gap: 4px;
}
.pagination-wrap {
  text-align: center;
  margin-top: 32px;
}
</style>
