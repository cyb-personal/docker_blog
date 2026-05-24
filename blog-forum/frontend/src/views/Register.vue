<template>
  <div class="register-page">
    <div class="register-bg"></div>
    <div class="register-container">
      <div class="register-card">
        <div class="register-header">
          <div class="logo-icon">
            <i class="el-icon-edit-outline"></i>
          </div>
          <h2>创建账号</h2>
          <p>加入博客论坛，开始你的分享之旅</p>
        </div>
        <el-form :model="form" :rules="rules" ref="formRef" class="register-form">
          <el-form-item prop="username">
            <el-input v-model="form.username" placeholder="用户名" prefix-icon="el-icon-user" size="large"></el-input>
          </el-form-item>
          <el-form-item prop="nickname">
            <el-input v-model="form.nickname" placeholder="昵称" prefix-icon="el-icon-s-custom" size="large"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" type="password" placeholder="密码" prefix-icon="el-icon-lock" size="large"></el-input>
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input v-model="form.confirmPassword" type="password" placeholder="确认密码" prefix-icon="el-icon-lock" size="large"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="large" class="register-btn" @click="handleRegister" :loading="loading">
              注 册
            </el-button>
          </el-form-item>
        </el-form>
        <div class="register-footer">
          已有账号？
          <router-link to="/login" class="login-link">立即登录</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { register } from '../api/user'

export default {
  name: 'Register',
  data() {
    return {
      form: { username: '', nickname: '', password: '', confirmPassword: '' },
      loading: false,
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }, { min: 6, message: '密码至少6位', trigger: 'blur' }],
        confirmPassword: [
          { required: true, message: '请确认密码', trigger: 'blur' },
          { validator: (rule, value, cb) => value === this.form.password ? cb() : cb(new Error('两次密码不一致')), trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    async handleRegister() {
      const valid = await this.$refs.formRef.validate().catch(() => {})
      if (!valid) return
      this.loading = true
      try {
        await register({
          username: this.form.username,
          password: this.form.password,
          nickname: this.form.nickname
        })
        this.$message.success('注册成功，请登录')
        this.$router.push('/login')
      } catch (e) {
        this.$message.error(e.message || '注册失败')
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.register-page {
  position: relative;
  height: 100vh;
  overflow: hidden;
}
.register-bg {
  position: absolute;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}
.register-container {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 100%;
  display: flex;
  justify-content: center;
}
.register-card {
  width: 420px;
  background: #fff;
  border-radius: 16px;
  padding: 40px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
}
.register-header {
  text-align: center;
  margin-bottom: 28px;
}
.logo-icon {
  width: 64px;
  height: 64px;
  line-height: 64px;
  border-radius: 16px;
  background: linear-gradient(135deg, #43e97b, #38f9d7);
  color: #fff;
  font-size: 28px;
  margin: 0 auto 16px;
}
.register-header h2 {
  font-size: 24px;
  color: #1a1a2e;
  margin-bottom: 6px;
}
.register-header p { color: #999; font-size: 14px; }
.register-form { margin-bottom: 20px; }
.register-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  border-radius: 8px;
  background: linear-gradient(135deg, #43e97b, #38f9d7);
  border: none;
}
.register-btn:hover { opacity: 0.9; }
.register-footer { text-align: center; color: #999; font-size: 14px; }
.login-link { color: #43e97b; text-decoration: none; font-weight: 500; }
.login-link:hover { text-decoration: underline; }
</style>
