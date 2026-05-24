<template>
  <div class="post-detail-page" v-loading="loading">
    <div class="post-detail-card" v-if="post">
      <div class="post-header">
        <router-link to="/posts" class="back-link">
          <i class="el-icon-arrow-left"></i> 返回列表
        </router-link>
      </div>
      <div class="post-meta">
        <div class="post-author">
          <el-avatar :size="48" class="author-avatar">{{ post.authorName ? post.authorName.charAt(0) : '?' }}</el-avatar>
          <div class="author-detail">
            <router-link :to="'/user/' + post.userId" class="author-name">{{ post.authorName || '未知用户' }}</router-link>
            <div class="post-info">
              <span>{{ formatTime(post.createTime) }}</span>
              <el-tag size="mini" type="success" v-if="post.category">{{ post.category }}</el-tag>
            </div>
          </div>
        </div>
      </div>
      <h1 class="post-title">{{ post.title }}</h1>
      <div class="post-content" v-html="renderContent(post.content)"></div>
      <div class="post-actions">
        <el-button
          :type="isLiked ? 'primary' : 'default'"
          size="medium"
          :icon="isLiked ? 'el-icon-star-on' : 'el-icon-star-off'"
          round
          @click="handleLike"
        >
          {{ isLiked ? '已点赞' : '点赞' }} {{ likeCount }}
        </el-button>
        <el-button size="medium" icon="el-icon-chat-line-square" round disabled>
          {{ commentCount }} 评论
        </el-button>
      </div>
    </div>

    <div class="comments-section">
      <div class="comments-header">
        <h3>评论 ({{ comments.length }})</h3>
      </div>
      <div class="comment-input-wrap">
        <el-input
          type="textarea"
          :rows="3"
          v-model="commentContent"
          placeholder="写下你的评论..."
          class="comment-input"
        ></el-input>
        <div class="comment-submit">
          <el-button type="primary" @click="handleAddComment" :disabled="!commentContent.trim()" round>发表评论</el-button>
        </div>
      </div>
      <div class="comments-list" v-loading="commentLoading">
        <div v-for="item in comments" :key="item.id" class="comment-item">
          <el-avatar :size="36" class="comment-avatar">{{ item.authorName ? item.authorName.charAt(0) : '?' }}</el-avatar>
          <div class="comment-body">
            <div class="comment-author">{{ item.authorName || '未知用户' }}</div>
            <div class="comment-text">{{ item.content }}</div>
            <div class="comment-time">{{ formatTime(item.createTime) }}</div>
          </div>
        </div>
        <el-empty v-if="!commentLoading && comments.length === 0" description="暂无评论，沙发等你来抢"></el-empty>
      </div>
    </div>
  </div>
</template>

<script>
import { getPostDetail } from '../api/post'
import { getComments, addComment, getCommentCount } from '../api/comment'
import { doLike, undoLike, getLikeStatus, getLikeCount } from '../api/like'

export default {
  name: 'PostDetail',
  data() {
    return {
      post: null,
      comments: [],
      loading: false,
      commentLoading: false,
      commentContent: '',
      isLiked: false,
      likeCount: 0,
      commentCount: 0
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    async loadData() {
      const id = this.$route.params.id
      this.loading = true
      try {
        const [post, comments, liveLikeCount, liveCommentCount] = await Promise.all([
          getPostDetail(id),
          getComments(id),
          getLikeCount(id).catch(() => 0),
          getCommentCount(id).catch(() => 0)
        ])
        this.post = post
        this.comments = comments || []
        this.likeCount = liveLikeCount
        this.commentCount = liveCommentCount
        const userId = sessionStorage.getItem('userId')
        if (userId) {
          const status = await getLikeStatus(post.id, userId).catch(() => null)
          if (status) {
            this.isLiked = status.liked
            this.likeCount = status.count
          }
        }
      } catch (e) {
        this.$message.error('加载失败')
      } finally {
        this.loading = false
      }
    },
    async handleLike() {
      const userId = sessionStorage.getItem('userId')
      if (!userId) { this.$message.warning('请先登录'); return }
      const postId = this.post.id
      try {
        if (this.isLiked) {
          const res = await undoLike({ postId: Number(postId), userId: Number(userId) })
          this.isLiked = res.liked
          this.likeCount = res.count
        } else {
          const res = await doLike({ postId: Number(postId), userId: Number(userId) })
          this.isLiked = res.liked
          this.likeCount = res.count
        }
      } catch (e) {
        this.$message.error(e.message || '操作失败')
      }
    },
    async handleAddComment() {
      const userId = sessionStorage.getItem('userId')
      if (!userId) { this.$message.warning('请先登录'); return }
      this.commentLoading = true
      try {
        const res = await addComment({
          postId: this.post.id,
          userId: Number(userId),
          content: this.commentContent
        })
        this.comments.push(res)
        this.commentCount = this.comments.length
        this.commentContent = ''
        this.$message.success('评论成功')
      } catch (e) {
        this.$message.error(e.message || '评论失败')
      } finally {
        this.commentLoading = false
      }
    },
    formatTime(time) {
      if (!time) return ''
      const d = new Date(time)
      const month = (d.getMonth() + 1).toString().padStart(2, '0')
      const day = d.getDate().toString().padStart(2, '0')
      const hour = d.getHours().toString().padStart(2, '0')
      const min = d.getMinutes().toString().padStart(2, '0')
      return `${month}-${day} ${hour}:${min}`
    },
    renderContent(content) {
      if (!content) return ''
      return content.replace(/\n/g, '<br>')
    }
  }
}
</script>

<style scoped>
.post-detail-page {
  max-width: 800px;
  margin: 0 auto;
  animation: fadeIn 0.3s ease;
}
@keyframes fadeIn { from { opacity: 0; transform: translateY(12px); } to { opacity: 1; transform: translateY(0); } }
.post-detail-card {
  background: #fff;
  border-radius: 12px;
  padding: 36px;
  margin-bottom: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}
.post-header { margin-bottom: 20px; }
.back-link {
  color: #667eea;
  text-decoration: none;
  font-size: 14px;
}
.back-link:hover { opacity: 0.8; }
.post-meta { margin-bottom: 20px; }
.post-author { display: flex; align-items: center; }
.author-avatar {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: #fff;
  margin-right: 14px;
}
.author-detail { display: flex; flex-direction: column; }
.author-name {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  text-decoration: none;
}
.author-name:hover { color: #667eea; }
.post-info {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 4px;
  font-size: 13px;
  color: #999;
}
.post-title {
  font-size: 26px;
  font-weight: 700;
  color: #1a1a2e;
  line-height: 1.4;
  margin-bottom: 20px;
}
.post-content {
  font-size: 15px;
  color: #333;
  line-height: 1.8;
  margin-bottom: 24px;
}
.post-actions {
  display: flex;
  gap: 12px;
  padding-top: 20px;
  border-top: 1px solid #f0f0f0;
}
.comments-section {
  background: #fff;
  border-radius: 12px;
  padding: 24px 36px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}
.comments-header h3 {
  font-size: 18px;
  color: #1a1a2e;
  margin-bottom: 20px;
}
.comment-input-wrap { margin-bottom: 24px; }
.comment-submit {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}
.comment-item {
  display: flex;
  padding: 16px 0;
  border-bottom: 1px solid #f5f5f5;
}
.comment-item:last-child { border-bottom: none; }
.comment-avatar {
  background: linear-gradient(135deg, #43e97b, #38f9d7);
  color: #fff;
  margin-right: 12px;
  flex-shrink: 0;
}
.comment-body { flex: 1; }
.comment-author {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 6px;
}
.comment-text {
  font-size: 14px;
  color: #555;
  line-height: 1.6;
  margin-bottom: 6px;
}
.comment-time { font-size: 12px; color: #bbb; }
</style>
