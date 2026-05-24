<template>
  <div class="app-layout">
    <NavHeader />
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script>
import NavHeader from '../components/NavHeader.vue'

export default {
  name: 'Home',
  components: { NavHeader }
}
</script>

<style scoped>
.app-layout {
  min-height: 100vh;
  background: #f0f2f5;
}
.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 84px 24px 40px;
}
</style>
