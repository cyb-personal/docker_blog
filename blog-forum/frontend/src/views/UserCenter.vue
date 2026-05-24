<template>
  <div class="user-center-page" v-loading="loading">
    <div class="user-profile-card">
      <div class="profile-bg"></div>
      <div class="profile-info">
        <el-avatar :size="80" class="profile-avatar">{{ user.nickname ? user.nickname.charAt(0) : '?' }}</el-avatar>
        <div class="profile-detail">
          <h2 class="profile-name">{{ user.nickname || '未知用户' }}</h2>
          <p class="profile-bio">{{ user.bio || '这个用户很懒，什么都没写...' }}</p>
          <div class="profile-meta">
            <span><i class="el-icon-user"></i> {{ user.username }}</span>
            <span><i class="el-icon-message"></i> {{ user.email || '未设置' }}</span>
            <span><i class="el-icon-date"></i> 加入于 {{ formatDate(user.createTime) }}</span>
          </div>
        </div>
        <el-button v-if="isSelf" type="primary" size="small" class="edit-btn" @click="showEdit = true" round>
          <i class="el-icon-edit"></i> 编辑资料
        </el-button>
      </div>
    </div>

    <div class="user-posts-section">
      <div class="section-header">
        <h3>TA 的帖子</h3>
      </div>
      <div v-if="posts.length > 0">
        <div v-for="post in posts" :key="post.id" class="post-item" @click="$router.push('/post/' + post.id)">
          <h4 class="post-title">{{ post.title }}</h4>
          <p class="post-excerpt">{{ truncateContent(post.content) }}</p>
          <div class="post-footer">
            <span>{{ formatTime(post.createTime) }}</span>
            <span><i class="el-icon-star-off"></i> {{ post.likeCount || 0 }}</span>
            <span><i class="el-icon-chat-line-square"></i> {{ post.commentCount || 0 }}</span>
          </div>
        </div>
      </div>
      <el-empty v-else description="暂无帖子"></el-empty>
    </div>

    <el-dialog title="编辑个人资料" :visible.sync="showEdit" width="500px" top="10vh">
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="昵称">
          <el-input v-model="editForm.nickname"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="editForm.email"></el-input>
        </el-form-item>
        <el-form-item label="简介">
          <el-input type="textarea" :rows="3" v-model="editForm.bio"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="showEdit = false">取消</el-button>
        <el-button type="primary" @click="handleUpdate">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getUserInfo, updateProfile } from '../api/user'
import { getUserPosts } from '../api/post'

export default {
  name: 'UserCenter',
  data() {
    return {
      user: {},
      posts: [],
      loading: false,
      showEdit: false,
      editForm: { nickname: '', email: '', bio: '' }
    }
  },
  computed: {
    isSelf() {
      return String(this.user.id) === sessionStorage.getItem('userId')
    }
  },
  created() {
    this.loadUserData()
  },
  methods: {
    async loadUserData() {
      const id = this.$route.params.id
      this.loading = true
      try {
        const [user, posts] = await Promise.all([
          getUserInfo(id),
          getUserPosts(id)
        ])
        this.user = user
        this.posts = posts || []
        this.editForm = { nickname: user.nickname, email: user.email, bio: user.bio }
      } catch (e) {
        this.$message.error('加载用户信息失败')
      } finally {
        this.loading = false
      }
    },
    async handleUpdate() {
      try {
        const res = await updateProfile({
          id: this.user.id,
          nickname: this.editForm.nickname,
          email: this.editForm.email,
          bio: this.editForm.bio
        })
        this.user = res
        sessionStorage.setItem('nickname', res.nickname)
        this.showEdit = false
        this.$message.success('更新成功')
      } catch (e) {
        this.$message.error(e.message || '更新失败')
      }
    },
    formatDate(time) {
      if (!time) return ''
      const d = new Date(time)
      return d.getFullYear() + '-' + ((d.getMonth() + 1) + '').padStart(2, '0')
    },
    formatTime(time) {
      if (!time) return ''
      const d = new Date(time)
      const month = (d.getMonth() + 1).toString().padStart(2, '0')
      const day = d.getDate().toString().padStart(2, '0')
      return `${month}-${day}`
    },
    truncateContent(content) {
      if (!content) return ''
      return content.length > 80 ? content.substring(0, 80) + '...' : content
    }
  }
}
</script>

<style scoped>
.user-center-page {
  max-width: 800px;
  margin: 0 auto;
  animation: fadeIn 0.3s ease;
}
@keyframes fadeIn { from { opacity: 0; transform: translateY(12px); } to { opacity: 1; transform: translateY(0); } }
.user-profile-card {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}
.profile-bg {
  height: 140px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.profile-info {
  display: flex;
  align-items: flex-start;
  padding: 0 28px 28px;
  margin-top: -40px;
  position: relative;
}
.profile-avatar {
  border: 4px solid #fff;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: #fff;
  flex-shrink: 0;
}
.profile-detail {
  flex: 1;
  margin-left: 20px;
  padding-top: 8px;
}
.profile-name {
  font-size: 22px;
  font-weight: 700;
  color: #1a1a2e;
}
.profile-bio {
  color: #999;
  font-size: 14px;
  margin-top: 4px;
}
.profile-meta {
  display: flex;
  gap: 20px;
  margin-top: 10px;
  font-size: 13px;
  color: #999;
}
.profile-meta i { margin-right: 4px; }
.edit-btn { margin-top: 28px; }
.user-posts-section {
  background: #fff;
  border-radius: 12px;
  padding: 24px 28px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}
.section-header h3 {
  font-size: 18px;
  color: #1a1a2e;
  margin-bottom: 16px;
}
.post-item {
  padding: 16px 0;
  border-bottom: 1px solid #f5f5f5;
  cursor: pointer;
  transition: all 0.2s;
}
.post-item:last-child { border-bottom: none; }
.post-item:hover { padding-left: 8px; }
.post-title {
  font-size: 16px;
  font-weight: 600;
  color: #1a1a2e;
  margin-bottom: 6px;
}
.post-excerpt {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin-bottom: 8px;
}
.post-footer {
  display: flex;
  gap: 16px;
  font-size: 12px;
  color: #bbb;
}
.post-footer i { margin-right: 3px; }
</style>
