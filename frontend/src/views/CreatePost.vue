<template>
  <div class="create-post-page">
    <div class="create-card">
      <div class="create-header">
        <h2>发布新帖子</h2>
        <p>分享你的想法、经验或问题</p>
      </div>
      <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入帖子标题" size="large" maxlength="100" show-word-limit></el-input>
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-select v-model="form.category" placeholder="选择分类" size="large" style="width: 200px">
            <el-option label="技术" value="技术"></el-option>
            <el-option label="生活" value="生活"></el-option>
            <el-option label="摄影" value="摄影"></el-option>
            <el-option label="读书" value="读书"></el-option>
            <el-option label="其他" value="其他"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input
            type="textarea"
            :rows="12"
            v-model="form.content"
            placeholder="写下你的内容..."
            maxlength="10000"
            show-word-limit
          ></el-input>
        </el-form-item>
        <el-form-item>
          <div class="form-actions">
            <el-button size="large" @click="$router.push('/')">取消</el-button>
            <el-button type="primary" size="large" @click="handleSubmit" :loading="submitting" round>
              发布帖子
            </el-button>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { createPost } from '../api/post'

export default {
  name: 'CreatePost',
  data() {
    return {
      form: { title: '', category: '技术', content: '' },
      submitting: false,
      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }, { min: 2, message: '标题至少2个字符', trigger: 'blur' }],
        content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
      }
    }
  },
  methods: {
    async handleSubmit() {
      const valid = await this.$refs.formRef.validate().catch(() => {})
      if (!valid) return
      this.submitting = true
      try {
        const userId = sessionStorage.getItem('userId')
        await createPost({
          userId: Number(userId),
          title: this.form.title,
          content: this.form.content,
          category: this.form.category
        })
        this.$message.success('发布成功')
        this.$router.push('/')
      } catch (e) {
        this.$message.error(e.message || '发布失败')
      } finally {
        this.submitting = false
      }
    }
  }
}
</script>

<style scoped>
.create-post-page {
  max-width: 800px;
  margin: 0 auto;
  animation: fadeIn 0.3s ease;
}
@keyframes fadeIn { from { opacity: 0; transform: translateY(12px); } to { opacity: 1; transform: translateY(0); } }
.create-card {
  background: #fff;
  border-radius: 12px;
  padding: 36px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}
.create-header {
  text-align: center;
  margin-bottom: 32px;
}
.create-header h2 {
  font-size: 24px;
  color: #1a1a2e;
  font-weight: 700;
}
.create-header p {
  color: #999;
  font-size: 14px;
  margin-top: 6px;
}
.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>
