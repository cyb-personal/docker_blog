<template>
  <div class="login-page">
    <div class="login-bg"></div>
    <div class="login-container">
      <div class="login-card">
        <div class="login-header">
          <div class="logo-icon">
            <i class="el-icon-chat-dot-round"></i>
          </div>
          <h2>博客论坛</h2>
          <p>分享知识，发现世界</p>
        </div>
        <el-form :model="form" :rules="rules" ref="formRef" class="login-form">
          <el-form-item prop="username">
            <el-input
              v-model="form.username"
              placeholder="请输入用户名"
              prefix-icon="el-icon-user"
              size="large"
            ></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              prefix-icon="el-icon-lock"
              size="large"
              @keyup.enter.native="handleLogin"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="large" class="login-btn" @click="handleLogin" :loading="loading">
              登 录
            </el-button>
          </el-form-item>
        </el-form>
        <div class="login-footer">
          还没有账号？
          <router-link to="/register" class="register-link">立即注册</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { login } from '../api/user'

export default {
  name: 'Login',
  data() {
    return {
      form: { username: 'admin', password: '123456' },
      loading: false,
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      }
    }
  },
  methods: {
    async handleLogin() {
      const valid = await this.$refs.formRef.validate().catch(() => {})
      if (!valid) return
      this.loading = true
      try {
        const res = await login(this.form)
        sessionStorage.setItem('token', res.token)
        sessionStorage.setItem('userId', res.userId)
        sessionStorage.setItem('nickname', res.nickname)
        this.$message.success('登录成功')
        this.$router.push('/')
      } catch (e) {
        this.$message.error(e.message || '登录失败')
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.login-page {
  position: relative;
  height: 100vh;
  overflow: hidden;
}
.login-bg {
  position: absolute;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.login-container {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 100%;
  display: flex;
  justify-content: center;
}
.login-card {
  width: 420px;
  background: #fff;
  border-radius: 16px;
  padding: 40px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
}
.login-header {
  text-align: center;
  margin-bottom: 32px;
}
.logo-icon {
  width: 64px;
  height: 64px;
  line-height: 64px;
  border-radius: 16px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: #fff;
  font-size: 28px;
  margin: 0 auto 16px;
}
.login-header h2 {
  font-size: 24px;
  color: #1a1a2e;
  margin-bottom: 6px;
}
.login-header p {
  color: #999;
  font-size: 14px;
}
.login-form { margin-bottom: 20px; }
.login-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border: none;
}
.login-btn:hover { opacity: 0.9; }
.login-footer {
  text-align: center;
  color: #999;
  font-size: 14px;
}
.register-link {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
}
.register-link:hover { text-decoration: underline; }
</style>
