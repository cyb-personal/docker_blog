<template>
  <header class="nav-header">
    <div class="header-inner">
      <div class="header-left">
        <router-link to="/" class="brand">
          <div class="brand-icon">
            <i class="el-icon-chat-dot-round"></i>
          </div>
          <span class="brand-text">博客论坛</span>
        </router-link>
      </div>
      <div class="header-center">
        <el-menu mode="horizontal" :default-active="activeMenu" router class="nav-menu">
          <el-menu-item index="/posts">
            <i class="el-icon-reading"></i>
            首页
          </el-menu-item>
          <el-menu-item index="/create">
            <i class="el-icon-edit"></i>
            发帖
          </el-menu-item>
        </el-menu>
      </div>
      <div class="header-right">
        <template v-if="isLoggedIn">
          <router-link :to="'/user/' + userId" class="user-info">
            <el-avatar :size="36" class="user-avatar">{{ nickname.charAt(0) }}</el-avatar>
            <span class="user-name">{{ nickname }}</span>
          </router-link>
          <el-button size="small" type="text" class="logout-btn" @click="handleLogout">
            <i class="el-icon-switch-button"></i> 退出
          </el-button>
        </template>
        <template v-else>
          <el-button size="small" @click="$router.push('/login')">登录</el-button>
          <el-button size="small" type="primary" @click="$router.push('/register')">注册</el-button>
        </template>
      </div>
    </div>
  </header>
</template>

<script>
export default {
  name: 'NavHeader',
  computed: {
    isLoggedIn() { return !!sessionStorage.getItem('token') },
    userId() { return sessionStorage.getItem('userId') },
    nickname() { return sessionStorage.getItem('nickname') || '用户' },
    activeMenu() { return this.$route.path }
  },
  methods: {
    handleLogout() {
      sessionStorage.clear()
      this.$message.success('已退出登录')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.nav-header {
  background: #fff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  height: 60px;
}
.header-inner {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  height: 100%;
  padding: 0 24px;
}
.header-left { display: flex; align-items: center; }
.brand {
  display: flex;
  align-items: center;
  text-decoration: none;
  margin-right: 40px;
}
.brand-icon {
  width: 36px;
  height: 36px;
  line-height: 36px;
  text-align: center;
  border-radius: 10px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: #fff;
  font-size: 18px;
  margin-right: 10px;
}
.brand-text {
  font-size: 18px;
  font-weight: 700;
  color: #1a1a2e;
}
.header-center { flex: 1; }
.nav-menu {
  border-bottom: none !important;
  background: transparent;
}
.nav-menu .el-menu-item {
  font-size: 14px;
  height: 60px;
  line-height: 60px;
  border-bottom: 2px solid transparent;
}
.nav-menu .el-menu-item.is-active {
  color: #667eea;
  border-bottom-color: #667eea;
}
.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}
.user-info {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: #333;
}
.user-avatar {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: #fff;
  margin-right: 8px;
}
.user-name {
  font-size: 14px;
  font-weight: 500;
}
.logout-btn { color: #999; }
.logout-btn:hover { color: #e74c3c; }
</style>
